package br.unaerp.view;

import br.unaerp.model.Transacao;
import br.unaerp.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GerenciarClassificacaoView extends JFrame {

    private JButton btnCadastrarClassificacao;
    private JButton btnEditarClassificacao;
    private JButton btnExcluirClassificacao;
    private JButton btnVoltar;
    private JTextField campoNomeClassificacao;
    private JTextField campoEditarClassificacao;
    private JComboBox<String> campoClassificacaoComboBox;
    private JComboBox<String> exclusaoComboBox;
    private Usuario usuario;

    public GerenciarClassificacaoView(Usuario usuario) {
        super("Sistema Financeiro");
        this.usuario = usuario;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel panelBotoes = new JPanel(new GridLayout(4, 1, 5, 10));
        btnCadastrarClassificacao = new JButton("Cadastrar Classificação");
        btnEditarClassificacao = new JButton("Editar Classificação");
        btnExcluirClassificacao = new JButton("Excluir Classificação");
        btnVoltar = new JButton("Voltar");

        panelBotoes.add(btnCadastrarClassificacao);
        panelBotoes.add(btnEditarClassificacao);
        panelBotoes.add(btnExcluirClassificacao);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCampos = new GridBagConstraints();
        gbcCampos.insets = new Insets(5, 5, 5, 5);
        gbcCampos.fill = GridBagConstraints.HORIZONTAL;

        gbcCampos.gridx = 0;
        gbcCampos.gridy = 0;
        campoNomeClassificacao = new JTextField(15);
        addPlaceholder(campoNomeClassificacao, "Nomeie a classificação");
        panelCampos.add(campoNomeClassificacao, gbcCampos);

        gbcCampos.gridx = 0;
        gbcCampos.gridy = 1;
        campoClassificacaoComboBox = new JComboBox<>(usuario.getClassificacoes().toArray(new String[0]));
        panelCampos.add(campoClassificacaoComboBox, gbcCampos);

        gbcCampos.gridx = 0;
        gbcCampos.gridy = 2;
        exclusaoComboBox = new JComboBox<>(usuario.getClassificacoes().toArray(new String[0]));
        panelCampos.add(exclusaoComboBox, gbcCampos);

        gbcCampos.gridx = 2;
        gbcCampos.gridy = 1;
        campoEditarClassificacao = new JTextField(15);
        addPlaceholder(campoEditarClassificacao, "Digite o novo nome");
        panelCampos.add(campoEditarClassificacao, gbcCampos);

        panel.add(panelCampos, gbc);

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(usuario).setVisible(true);
                dispose();
            }
        });

        btnCadastrarClassificacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String novaClassificacao = campoNomeClassificacao.getText();
                String mensagem = usuario.adicionarClassificacao(novaClassificacao);
                JOptionPane.showMessageDialog(GerenciarClassificacaoView.this, mensagem);
                atualizarTela();
            }
        });

        btnEditarClassificacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String classificacaoAtual = (String) campoClassificacaoComboBox.getSelectedItem();
                String novaClassificacao = campoEditarClassificacao.getText();
                String mensagem = usuario.editarClassificacao(classificacaoAtual, novaClassificacao);
                JOptionPane.showMessageDialog(GerenciarClassificacaoView.this, mensagem);
                atualizarTela();
            }
        });

        btnExcluirClassificacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String classificacaoExcluir = (String) exclusaoComboBox.getSelectedItem();
                String mensagem = usuario.excluirClassificacao(classificacaoExcluir);
                JOptionPane.showMessageDialog(GerenciarClassificacaoView.this, mensagem);
                atualizarTela();
            }
        });

        add(panel);
    }


    private void atualizarTela() {
        DefaultComboBoxModel<String> novoModelo = new DefaultComboBoxModel<>(usuario.getClassificacoes().toArray(new String[0]));
        campoClassificacaoComboBox.setModel(novoModelo);
        exclusaoComboBox.setModel(new DefaultComboBoxModel<>(usuario.getClassificacoes().toArray(new String[0])));

        campoNomeClassificacao.setText("");
        addPlaceholder(campoNomeClassificacao, "Nomeie a classificação");
        campoEditarClassificacao.setText("");
        addPlaceholder(campoEditarClassificacao, "Digite o novo nome");
    }


    private void addPlaceholder(JTextField field, String placeholder) {
        //precisa de ajustes, os botões estão utilizando o placeholder como parâmetro
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    public JButton getBtnEditarClassificacao() {
        return btnEditarClassificacao;
    }

    public JButton getBtnExcluirClassificacao() {
        return btnExcluirClassificacao;
    }

    public JTextField getCampoNomeClassificacao() {
        return campoNomeClassificacao;
    }
}

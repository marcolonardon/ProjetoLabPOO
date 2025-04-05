package br.unaerp.view;

import br.unaerp.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GerenciarCategoriaView extends JFrame {

    private JButton btnCadastrarCategoria;
    private JButton btnEditarCategoria;
    private JButton btnExcluirCategoria;
    private JButton btnVoltar;
    private JTextField campoNomeCategoria;
    private JTextField campoEditarCategoria;
    private JComboBox<String> campoCategoriaComboBox;
    private JComboBox<String> exclusaoComboBox;
    private Usuario usuario;

    public GerenciarCategoriaView(Usuario usuario) {
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
        btnCadastrarCategoria = new JButton("Cadastrar Categoria");
        btnEditarCategoria = new JButton("Editar Categoria");
        btnExcluirCategoria = new JButton("Excluir Categoria");
        btnVoltar = new JButton("Voltar");

        panelBotoes.add(btnCadastrarCategoria);
        panelBotoes.add(btnEditarCategoria);
        panelBotoes.add(btnExcluirCategoria);
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
        campoNomeCategoria = new JTextField(15);
        addPlaceholder(campoNomeCategoria, "Nomeie a categoria");
        panelCampos.add(campoNomeCategoria, gbcCampos);

        gbcCampos.gridx = 0;
        gbcCampos.gridy = 1;
        campoCategoriaComboBox = new JComboBox<>(usuario.getCategorias().toArray(new String[0]));
        panelCampos.add(campoCategoriaComboBox, gbcCampos);

        gbcCampos.gridx = 0;
        gbcCampos.gridy = 2;
        exclusaoComboBox = new JComboBox<>(usuario.getCategorias().toArray(new String[0]));
        panelCampos.add(exclusaoComboBox, gbcCampos);

        gbcCampos.gridx = 2;
        gbcCampos.gridy = 1;
        campoEditarCategoria = new JTextField(15);
        addPlaceholder(campoEditarCategoria, "Digite o novo nome");
        panelCampos.add(campoEditarCategoria, gbcCampos);

        panel.add(panelCampos, gbc);

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(usuario).setVisible(true);
                dispose();
            }
        });

        btnCadastrarCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String novaCategoria = campoNomeCategoria.getText();
                String mensagem = usuario.adicionarCategoria(novaCategoria);
                JOptionPane.showMessageDialog(GerenciarCategoriaView.this, mensagem);
                atualizarTela();
            }
        });

        btnEditarCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoriaAtual = (String) campoCategoriaComboBox.getSelectedItem();
                String novaCategoria = campoEditarCategoria.getText();
                String mensagem = usuario.editarCategoria(categoriaAtual, novaCategoria);
                JOptionPane.showMessageDialog(GerenciarCategoriaView.this, mensagem);
                atualizarTela();
            }
        });

        btnExcluirCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoriaExcluir = (String) exclusaoComboBox.getSelectedItem();
                String mensagem = usuario.excluirCategoria(categoriaExcluir);
                JOptionPane.showMessageDialog(GerenciarCategoriaView.this, mensagem);
                atualizarTela();
            }
        });

        add(panel);
    }


    private void atualizarTela() {
        DefaultComboBoxModel<String> novoModelo = new DefaultComboBoxModel<>(usuario.getCategorias().toArray(new String[0]));
        campoCategoriaComboBox.setModel(novoModelo);
        exclusaoComboBox.setModel(new DefaultComboBoxModel<>(usuario.getCategorias().toArray(new String[0])));

        campoNomeCategoria.setText("");
        addPlaceholder(campoNomeCategoria, "Nomeie a categoria");
        campoEditarCategoria.setText("");
        addPlaceholder(campoEditarCategoria, "Digite o novo nome");
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

    public JButton getBtnEditarCategoria() {
        return btnEditarCategoria;
    }

    public JButton getBtnExcluirCategoria() {
        return btnExcluirCategoria;
    }

    public JTextField getCampoNomeCategoria() {
        return campoNomeCategoria;
    }
}

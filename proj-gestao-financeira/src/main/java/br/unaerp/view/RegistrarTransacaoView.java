package br.unaerp.view;

import br.unaerp.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrarTransacaoView extends JFrame {
    private JTextField campoValor;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> campoClassificacao;
    private JTextField campoDescricao;
    private JTextField campoDia;
    private JTextField campoMes;
    private JTextField campoAno;
    private JButton btnRegistrar;
    private JButton btnVoltar;

    private Usuario usuario;

    public RegistrarTransacaoView(Usuario usuario) {
        super("Registrar Nova Transação");
        this.usuario = usuario;
        initComponents();
    }

    private void initComponents() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Valor:"), gbc);
        campoValor = new JTextField(10);
        gbc.gridx = 1;
        panel.add(campoValor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Categoria:"), gbc);
        comboCategoria = new JComboBox<>(new String[]{"Receita", "Despesa"});
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Classificação:"), gbc);
        campoClassificacao = new JComboBox<>(usuario.getClassificacoes().toArray(new String[0]));
        gbc.gridx = 1;
        panel.add(campoClassificacao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Descrição:"), gbc);
        campoDescricao = new JTextField(10);
        gbc.gridx = 1;
        panel.add(campoDescricao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Dia:"), gbc);
        campoDia = new JTextField(3);
        gbc.gridx = 1;
        panel.add(campoDia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Mês:"), gbc);
        campoMes = new JTextField(3);
        gbc.gridx = 1;
        panel.add(campoMes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Ano:"), gbc);
        campoAno = new JTextField(4);
        gbc.gridx = 1;
        panel.add(campoAno, gbc);

        btnRegistrar = new JButton("Registrar");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(btnRegistrar, gbc);

        btnVoltar = new JButton("Voltar");
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(btnVoltar, gbc);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarTransacao();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(usuario).setVisible(true);
                dispose();
            }
        });

        add(panel);
    }

    private void registrarTransacao() {
        try {
            float valor = Float.parseFloat(campoValor.getText());
            String categoria = (String) comboCategoria.getSelectedItem();
            String classificacao = (String) campoClassificacao.getSelectedItem();
            String descricao = campoDescricao.getText();
            int dia = Integer.parseInt(campoDia.getText());
            int mes = Integer.parseInt(campoMes.getText());
            int ano = Integer.parseInt(campoAno.getText());

            usuario.registrarTransacao(valor, categoria, classificacao, descricao, dia, mes, ano);

            JOptionPane.showMessageDialog(this, "Transação registrada com sucesso!");

            new MainView(usuario).setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos informados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

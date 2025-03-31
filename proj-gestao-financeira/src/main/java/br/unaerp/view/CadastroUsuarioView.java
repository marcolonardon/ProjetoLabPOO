package br.unaerp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CadastroUsuarioView extends JFrame {
    private JTextField campoLogin;
    private JTextField campoNome;
    private JComboBox<String> campoTipo;
    private JTextField campoDocumento;
    private JPasswordField campoSenha;
    private JButton botaoSalvar;
    private JButton botaoVoltar;

    public CadastroUsuarioView() {
        setTitle("Cadastro de Novo Usuário");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Margens entre os componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel labelLogin = new JLabel("Usuário:");
        campoLogin = new JTextField(20);

        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(20);

        JLabel labelTipo = new JLabel("Tipo:");
        campoTipo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});

        JLabel labelDocumento = new JLabel("CPF/CNPJ:");
        campoDocumento = new JTextField(20);

        JLabel labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);

        botaoSalvar = new JButton("Salvar");
        botaoVoltar = new JButton("Voltar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(labelLogin, gbc);

        gbc.gridx = 1;
        add(campoLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(labelNome, gbc);

        gbc.gridx = 1;
        add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelTipo, gbc);

        gbc.gridx = 1;
        add(campoTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(labelDocumento, gbc);

        gbc.gridx = 1;
        add(campoDocumento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(labelSenha, gbc);

        gbc.gridx = 1;
        add(campoSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(botaoSalvar, gbc);

        gbc.gridx = 1;
        add(botaoVoltar, gbc);

        botaoSalvar.setPreferredSize(new Dimension(120, 40));

        botaoVoltar.setPreferredSize(new Dimension(120, 40));
    }

    public String getLogin() {
        return campoLogin.getText();
    }

    public String getNome() {
        return campoNome.getText();
    }

    public String getTipoUsuario() {
        return (String) campoTipo.getSelectedItem();
    }

    public String getDocumento() {
        return campoDocumento.getText();
    }

    public String getSenha() {
        return new String(campoSenha.getPassword());
    }

    public void addSalvarListener(ActionListener listener) {
        botaoSalvar.addActionListener(listener);
    }

    public void addVoltarListener(ActionListener listener) {
        botaoVoltar.addActionListener(listener);
    }
}

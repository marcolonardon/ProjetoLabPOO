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
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        JLabel labelLogin = new JLabel("Usuário:");
        campoLogin = new JTextField();

        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField();

        JLabel labelTipo = new JLabel("Tipo:");
        campoTipo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});

        JLabel labelDocumento = new JLabel("CPF/CNPJ:");
        campoDocumento = new JTextField();

        JLabel labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField();

        botaoSalvar = new JButton("Salvar");
        botaoVoltar = new JButton("Voltar");

        add(labelLogin);
        add(campoLogin);
        add(labelNome);
        add(campoNome);
        add(labelTipo);
        add(campoTipo);
        add(labelDocumento);
        add(campoDocumento);
        add(labelSenha);
        add(campoSenha);
        add(botaoSalvar);
        add(botaoVoltar);
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

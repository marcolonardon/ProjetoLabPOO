package br.unaerp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnNovoUsuario;

    public LoginView() {
        super("Tela de Login");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuário:"), gbc);

        txtUsuario = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);

        txtSenha = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtSenha, gbc);

        btnEntrar = new JButton("Entrar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(btnEntrar, gbc);

        btnNovoUsuario = new JButton("Criar Novo Usuário");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(btnNovoUsuario, gbc);

        add(panel);
    }

    public String getUsuario() {
        return txtUsuario.getText();
    }

    public String getSenha() {
        return new String(txtSenha.getPassword());
    }

    public void addEntrarListener(ActionListener listener) {
        btnEntrar.addActionListener(listener);
    }

    public void addNovoUsuarioListener(ActionListener listener) {
        btnNovoUsuario.addActionListener(listener);
    }
}

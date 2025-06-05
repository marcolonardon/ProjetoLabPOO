package br.unaerp.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnNovoUsuario;

    public LoginView() {
        super("Bem-vindo(a)");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Bem-vindo(a)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBorder(new EmptyBorder(20, 0, 0, 0));
        painelTitulo.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(new EmptyBorder(0, 10, 20, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuário:"), gbc);

        txtUsuario = new JTextField(20);
        gbc.gridx = 1;
        panelFormulario.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Senha:"), gbc);

        txtSenha = new JPasswordField(20);
        gbc.gridx = 1;
        panelFormulario.add(txtSenha, gbc);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(btnEntrar, gbc);

        btnNovoUsuario = new JButton("Criar Novo Usuário");
        btnNovoUsuario.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        panelFormulario.add(btnNovoUsuario, gbc);

        add(painelTitulo, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
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

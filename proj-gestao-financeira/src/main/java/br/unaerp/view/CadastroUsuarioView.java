package br.unaerp.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        super("Cadastrar Usuário");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Cadastro de Usuário", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(0, 10, 10, 10)); // topo reduzido
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(labelLogin, gbc);
        gbc.gridx = 1;
        formPanel.add(campoLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(labelNome, gbc);
        gbc.gridx = 1;
        formPanel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(labelTipo, gbc);
        gbc.gridx = 1;
        formPanel.add(campoTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(labelDocumento, gbc);
        gbc.gridx = 1;
        formPanel.add(campoDocumento, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(labelSenha, gbc);
        gbc.gridx = 1;
        formPanel.add(campoSenha, gbc);

        botaoSalvar = new JButton("Salvar");
        botaoVoltar = new JButton("Voltar");
        botaoSalvar.setPreferredSize(new Dimension(100, 30));
        botaoVoltar.setPreferredSize(new Dimension(100, 30));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.add(botaoSalvar);
        btnPanel.add(botaoVoltar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    public String getLogin() { return campoLogin.getText().trim(); }
    public String getNome() { return campoNome.getText().trim(); }
    public String getTipoUsuario() { return (String) campoTipo.getSelectedItem(); }
    public String getDocumento() { return campoDocumento.getText().trim(); }
    public String getSenha() { return new String(campoSenha.getPassword()); }

    public void addSalvarListener(ActionListener listener) { botaoSalvar.addActionListener(listener); }
    public void addVoltarListener(ActionListener listener) { botaoVoltar.addActionListener(listener); }
}
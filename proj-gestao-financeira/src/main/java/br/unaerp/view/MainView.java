package br.unaerp.view;

import br.unaerp.controller.LoginController;
import br.unaerp.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JLabel nomeLabel;
    private JLabel saldoLabel;

    private JButton btnCadastrarTransacao;
    private JButton btnGerenciarCategorias;
    private JButton btnVisualizarTransacoes;
    private JButton btnLogout;

    private Usuario usuario;

    public MainView(Usuario usuario) {
        super("Sistema Financeiro");
        this.usuario = usuario;
        initComponents();
        updateInfo();

        btnCadastrarTransacao.addActionListener(e -> {
            new RegistrarTransacaoView(usuario).setVisible(true);
            dispose();
        });

        btnGerenciarCategorias.addActionListener(e -> {
            new GerenciarCategoriaView(usuario).setVisible(true);
            dispose();
        });

        btnVisualizarTransacoes.addActionListener(e -> {
            new VisualizarTransacaoView(usuario).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
            dispose();
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomeLabel = new JLabel();
        saldoLabel = new JLabel();
        nomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(nomeLabel, gbc);

        gbc.gridx = 1;
        panel.add(saldoLabel, gbc);

        gbc.gridx = 2;
        btnLogout = new JButton("Logout");
        btnLogout.setPreferredSize(new Dimension(80, 30));
        panel.add(btnLogout, gbc);

        btnCadastrarTransacao = new JButton("Cadastrar Nova Transação");
        btnCadastrarTransacao.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(btnCadastrarTransacao, gbc);

        btnGerenciarCategorias = new JButton("Gerenciar Categorias");
        btnGerenciarCategorias.setPreferredSize(new Dimension(200, 30));
        gbc.gridy = 2;
        panel.add(btnGerenciarCategorias, gbc);

        btnVisualizarTransacoes = new JButton("Visualizar Transações");
        btnVisualizarTransacoes.setPreferredSize(new Dimension(200, 30));
        gbc.gridy = 3;
        panel.add(btnVisualizarTransacoes, gbc);

        add(panel);
    }

    public void updateInfo() {
        nomeLabel.setText("Olá, " + usuario.getNome() + "!");
        saldoLabel.setText("Saldo: R$ " + String.format("%.2f", usuario.calcularSaldoTotal()));
    }

    public JButton getBtnCadastrarTransacao() {
        return btnCadastrarTransacao;
    }

    public JButton getBtnVisualizarTransacoes() {
        return btnVisualizarTransacoes;
    }
}
package br.unaerp.view;

import br.unaerp.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            RegistrarTransacaoView novaTransacaoView = new RegistrarTransacaoView(usuario);
            novaTransacaoView.setVisible(true);
            dispose();
        });

        btnGerenciarCategorias.addActionListener(e -> {
            GerenciarCategoriaView categoriaView = new GerenciarCategoriaView(usuario);
            categoriaView.setVisible(true);
            dispose();
        });

        btnVisualizarTransacoes.addActionListener(e -> {
            VisualizarTransacaoView visualizarTransacaoView = new VisualizarTransacaoView(usuario);
            visualizarTransacaoView.setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });
            dispose();
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);
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
        panel.add(btnLogout, gbc);

        btnCadastrarTransacao = new JButton("Cadastrar Nova Transação");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(btnCadastrarTransacao, gbc);

        btnGerenciarCategorias = new JButton("Gerenciar Categorias");
        gbc.gridy = 2;
        panel.add(btnGerenciarCategorias, gbc);

        btnVisualizarTransacoes = new JButton("Visualizar Transações");
        gbc.gridy = 3;
        panel.add(btnVisualizarTransacoes, gbc);

        add(panel);
    }

    public void updateInfo() {
        nomeLabel.setText("Olá, " + usuario.getNome() + "!");
        saldoLabel.setText("Saldo: R$" + String.format("%.2f", usuario.calcularSaldoTotal()));
    }

    public JButton getBtnCadastrarTransacao() {
        return btnCadastrarTransacao;
    }

    public JButton getBtnVisualizarTransacoes() {
        return btnVisualizarTransacoes;
    }
}

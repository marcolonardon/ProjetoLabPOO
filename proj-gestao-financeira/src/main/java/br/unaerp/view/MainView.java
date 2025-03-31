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
    private JButton btnGerenciarClassificacoes;
    private JButton btnVisualizarTransacoes;

    private Usuario usuario;

    public MainView(Usuario usuario) {
        super("Sistema Financeiro");
        this.usuario = usuario;
        initComponents();
        updateInfo();

        btnCadastrarTransacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarTransacaoView novaTransacaoView = new RegistrarTransacaoView(usuario);
                novaTransacaoView.setVisible(true);
                dispose();
            }
        });

        btnGerenciarClassificacoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GerenciarClassificacaoView classificacoesView = new GerenciarClassificacaoView(usuario);
                classificacoesView.setVisible(true);
                dispose();
            }
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 400);
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
        gbc.gridwidth = 2;
        panel.add(nomeLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        panel.add(saldoLabel, gbc);

        btnCadastrarTransacao = new JButton("Cadastrar Nova Transação");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(btnCadastrarTransacao, gbc);

        btnGerenciarClassificacoes = new JButton("Gerenciar Classificações");
        gbc.gridy = 2;
        panel.add(btnGerenciarClassificacoes, gbc);

        btnVisualizarTransacoes = new JButton("Visualizar Transações");
        gbc.gridy = 5;
        panel.add(btnVisualizarTransacoes, gbc);

        add(panel);
    }

    public void updateInfo() {
        nomeLabel.setText("Olá, " + usuario.getNome() + "!");
        saldoLabel.setText("Saldo: R$" + usuario.calcularSaldoTotal());
    }

    public JButton getBtnCadastrarTransacao() {
        return btnCadastrarTransacao;
    }

    public JButton getBtnVisualizarTransacoes() {
        return btnVisualizarTransacoes;
    }
}

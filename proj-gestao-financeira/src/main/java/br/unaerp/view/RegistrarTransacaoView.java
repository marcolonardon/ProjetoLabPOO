package br.unaerp.view;

import br.unaerp.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegistrarTransacaoView extends JFrame {
    private JTextField campoValor;
    private JComboBox<String> comboClassificacao;
    private JComboBox<String> comboCategoria;
    private JTextField campoDescricao;
    private JFormattedTextField campoData;
    private JButton btnRegistrar;
    private JButton btnVoltar;

    private Usuario usuario;

    public RegistrarTransacaoView(Usuario usuario) {
        super("Registrar Nova Transação");
        this.usuario = usuario;
        initComponents();
    }

    private void initComponents() {
        setSize(650, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Registrar Nova Transação", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 5, 0));
        add(lblTitulo, BorderLayout.NORTH);

        add(new JSeparator(), BorderLayout.AFTER_LAST_LINE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("*Valor (R$):"), gbc);
        campoValor = new JTextField(10);
        gbc.gridx = 1;
        panel.add(campoValor, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("*Data (dd/MM/yyyy):"), gbc);
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            campoData = new JFormattedTextField(mask);
        } catch (Exception ex) {
            campoData = new JFormattedTextField();
        }
        campoData.setColumns(8);
        gbc.gridx = 1;
        panel.add(campoData, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("*Classificação:"), gbc);
        comboClassificacao = new JComboBox<>(new String[]{"Receita", "Despesa"});
        gbc.gridx = 1;
        panel.add(comboClassificacao, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("*Categoria:"), gbc);
        comboCategoria = new JComboBox<>(usuario.getCategoria().getCategorias().toArray(new String[0]));
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("*Descrição:"), gbc);
        campoDescricao = new JTextField(15);
        gbc.gridx = 1;
        panel.add(campoDescricao, gbc);

        y++;
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnRegistrar, gbc);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnVoltar, gbc);

        add(panel, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarTransacao());
        btnVoltar.addActionListener(e -> {
            new MainView(usuario).setVisible(true);
            dispose();
        });
    }

    private void registrarTransacao() {
        StringBuilder erros = new StringBuilder();
        String valorStr = campoValor.getText().trim();
        String dataStr = campoData.getText().trim();
        String descricao = campoDescricao.getText().trim();

        if (valorStr.isEmpty()) {
            erros.append("- Valor é obrigatório\n");
        }
        if (dataStr.isEmpty() || dataStr.contains("_")) {
            erros.append("- Data é obrigatória (DD/MM/AAAA)\n");
        }
        if (descricao.isEmpty()) {
            erros.append("- Descrição é obrigatória\n");
        }
        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, verifique os seguintes campos:\n" + erros.toString(),
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        try {
            float valor = Float.parseFloat(valorStr.replace(',', '.'));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataStr, fmt);

            String classificacao = (String) comboClassificacao.getSelectedItem();
            String categoria = (String) comboCategoria.getSelectedItem();

            usuario.registrarTransacao(
                    valor,
                    categoria,
                    classificacao,
                    descricao,
                    data.getDayOfMonth(),
                    data.getMonthValue(),
                    data.getYear()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Transação registrada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE
            );
            new MainView(usuario).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Valor inválido. Use apenas números.",
                    "Erro", JOptionPane.ERROR_MESSAGE
            );
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data inválida. Verifique o formato dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
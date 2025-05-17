package br.unaerp.view;

import br.unaerp.model.Usuario;
import javax.swing.*;
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
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        // Valor
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Valor (R$):"), gbc);
        campoValor = new JTextField(10);
        campoValor.setToolTipText("Digite o valor da transação");
        gbc.gridx = 1;
        panel.add(campoValor, gbc);

        // Data
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Data (dd/mm/yyyy):"), gbc);
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            campoData = new JFormattedTextField(mask);
        } catch (Exception ex) {
            campoData = new JFormattedTextField();
        }
        campoData.setColumns(8);
        campoData.setToolTipText("Formato: dd/mm/yyyy");
        gbc.gridx = 1;
        panel.add(campoData, gbc);

        // Classificação
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Classificação:"), gbc);
        comboClassificacao = new JComboBox<>(new String[]{"Receita", "Despesa"});
        gbc.gridx = 1;
        panel.add(comboClassificacao, gbc);

        // Categoria
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Categoria:"), gbc);
        comboCategoria = new JComboBox<>(usuario.getCategoria().getCategorias().toArray(new String[0]));
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        // Descrição
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Descrição:"), gbc);
        campoDescricao = new JTextField(15);
        campoDescricao.setToolTipText("Breve descrição da transação");
        gbc.gridx = 1;
        panel.add(campoDescricao, gbc);

        // Botões
        y++;
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setPreferredSize(new Dimension(120, 28));
        btnRegistrar.addActionListener(e -> registrarTransacao());
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnRegistrar, gbc);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(120, 28));
        btnVoltar.addActionListener(e -> {
            new MainView(usuario).setVisible(true);
            dispose();
        });
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnVoltar, gbc);

        add(panel);
    }

    private void registrarTransacao() {
        try {
            float valor = Float.parseFloat(campoValor.getText().replace(',', '.'));
            String dataStr = campoData.getText();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataStr, fmt);

            String classificacao = (String) comboClassificacao.getSelectedItem();
            String categoria = (String) comboCategoria.getSelectedItem();
            String descricao = campoDescricao.getText().trim();

            usuario.registrarTransacao(
                    valor,
                    categoria,
                    classificacao,
                    descricao,
                    data.getDayOfMonth(),
                    data.getMonthValue(),
                    data.getYear()
            );

            JOptionPane.showMessageDialog(this,
                    "Transação registrada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            new MainView(usuario).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valor inválido. Use apenas números.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Data inválida. Verifique o formato dd/mm/yyyy e valores válidos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

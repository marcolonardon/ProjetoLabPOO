package br.unaerp.view;

import br.unaerp.model.Categoria;
import br.unaerp.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RegistrarTransacaoView extends JFrame {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Usuario usuario;

    private JFormattedTextField campoValor;
    private JComboBox<String> comboClassificacao;
    private JComboBox<Categoria> comboCategoria;
    private JTextField campoDescricao;
    private JFormattedTextField campoData;
    private JButton btnRegistrar;
    private JButton btnVoltar;

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

        // Valor
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("*Valor (R$):"), gbc);

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        NumberFormatter vf = new NumberFormatter(nf);
        vf.setValueClass(Double.class);
        vf.setAllowsInvalid(false);
        vf.setOverwriteMode(true);
        campoValor = new JFormattedTextField(vf);
        campoValor.setColumns(10);
        gbc.gridx = 1;
        panel.add(campoValor, gbc);

        // Data
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("*Data (dd/MM/yyyy):"), gbc);
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            campoData = new JFormattedTextField(mask);
        } catch (ParseException ex) {
            campoData = new JFormattedTextField();
        }
        campoData.setColumns(8);
        gbc.gridx = 1;
        panel.add(campoData, gbc);

        // Classificação
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("*Classificação:"), gbc);
        comboClassificacao = new JComboBox<>(new String[]{"Receita", "Despesa"});
        gbc.gridx = 1;
        panel.add(comboClassificacao, gbc);

        // Categoria
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("*Categoria:"), gbc);
        comboCategoria = new JComboBox<>();
        comboCategoria.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        // Descrição
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("*Descrição:"), gbc);
        campoDescricao = new JTextField(15);
        gbc.gridx = 1;
        panel.add(campoDescricao, gbc);

        // Botões
        y++;
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnRegistrar, gbc);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnVoltar, gbc);

        add(panel, BorderLayout.CENTER);
    }

    public void setListaCategorias(List<Categoria> listaCategorias) {
        DefaultComboBoxModel<Categoria> model = new DefaultComboBoxModel<>();
        for (Categoria c : listaCategorias) {
            model.addElement(c);
        }
        comboCategoria.setModel(model);

        comboCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Categoria) {
                    setText(((Categoria) value).getNome());
                }
                return this;
            }
        });
    }

    public String getValor() {
        Object val = campoValor.getValue();
        if (val == null) {
            return "";
        }
        return String.format(Locale.forLanguageTag("pt-BR"), "%.2f", ((Number) val).doubleValue());
    }

    public String getData() {
        return campoData.getText().trim();
    }

    public String getDescricao() {
        return campoDescricao.getText().trim();
    }

    public String getClassificacao() {
        return (String) comboClassificacao.getSelectedItem();
    }

    public Categoria getCategoriaSelecionada() {
        return (Categoria) comboCategoria.getSelectedItem();
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public void clearCampos() {
        campoValor.setValue(null);
        campoData.setText("");
        campoDescricao.setText("");
        comboClassificacao.setSelectedIndex(0);
        campoValor.requestFocus();
    }

    public void setValor(String valor) {
        try {
            Number parsed = NumberFormat.getNumberInstance(new Locale("pt", "BR")).parse(valor);
            campoValor.setValue(parsed.doubleValue());
        } catch (ParseException ignored) {
        }
    }

    public void setData(String data) {
        campoData.setText(data);
    }

    public void setDescricao(String descricao) {
        campoDescricao.setText(descricao);
    }

    public void setClassificacao(String classificacao) {
        comboClassificacao.setSelectedItem(classificacao);
    }

    public void setCategoriaSelecionada(Categoria categoria) {
        comboCategoria.setSelectedItem(categoria);
    }
}

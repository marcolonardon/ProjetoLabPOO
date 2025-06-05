package br.unaerp.view;

import br.unaerp.model.Categoria;
import br.unaerp.model.CategoriaDAO;
import br.unaerp.model.CategoriaDAOImpl;
import br.unaerp.model.Transacao;
import br.unaerp.model.TransacaoDAO;
import br.unaerp.model.TransacaoDAOImpl;
import br.unaerp.model.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizarTransacaoView extends JFrame {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Usuario usuario;
    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();
    private JTable table;
    private DefaultTableModel tableModel;
    private JFormattedTextField txtDataInicio;
    private JFormattedTextField txtDataFim;
    private JCheckBox chkTodas, chkReceita, chkDespesa;
    private JCheckBox chkTodasCats;
    private List<JCheckBox> chkCategorias;
    private JButton btnFiltrar, btnAtualizar, btnVoltarFiltros, btnVoltarTransacoes;
    private JLabel lblResumo;
    private JTabbedPane tabPane;
    private static final int COL_EDITAR = 5;
    private static final int COL_EXCLUIR = 6;
    private List<Transacao> todasTransacoes;

    public VisualizarTransacaoView(Usuario usuario) {
        super("Visualizar Transações");
        this.usuario = usuario;

        todasTransacoes = transacaoDAO.buscarPorUsuario(usuario.getLogin());

        initComponents();

        carregarTabela(todasTransacoes, null, null);
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 480);
        setLocationRelativeTo(null);

        tabPane = new JTabbedPane();

        // Aba Transações
        JPanel panelTransacoes = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Data", "Descrição", "Valor", "Classificação", "Categoria", "Editar", "Excluir"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return (col == COL_EDITAR || col == COL_EXCLUIR);
            }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
        table.getColumnModel().getColumn(COL_EDITAR).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_EDITAR).setMaxWidth(80);
        table.getColumnModel().getColumn(COL_EXCLUIR).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_EXCLUIR).setMaxWidth(80);
        table.getColumn("Editar").setCellRenderer(new ButtonRenderer());
        table.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
        table.getColumn("Excluir").setCellEditor(new ButtonEditor(new JCheckBox()));

        panelTransacoes.add(new JScrollPane(table), BorderLayout.CENTER);

        // Resumo
        lblResumo = new JLabel(
                "Período: (total) - Total Receitas: R$ 0.00   Total Despesas: R$ 0.00",
                SwingConstants.CENTER
        );
        lblResumo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Botão Voltar
        btnVoltarTransacoes = new JButton("Voltar");
        btnVoltarTransacoes.setPreferredSize(new Dimension(100, 30));
        btnVoltarTransacoes.addActionListener(e -> {
            new MainView(usuario).setVisible(true);
            dispose();
        });
        JPanel backPanelTrans = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanelTrans.add(btnVoltarTransacoes);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(lblResumo, BorderLayout.NORTH);
        southContainer.add(new JSeparator(), BorderLayout.CENTER);
        southContainer.add(backPanelTrans, BorderLayout.SOUTH);
        panelTransacoes.add(southContainer, BorderLayout.SOUTH);

        // Aba Filtros
        JPanel filtroPanel = new JPanel();
        filtroPanel.setLayout(new BoxLayout(filtroPanel, BoxLayout.Y_AXIS));
        filtroPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Período
        JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        periodoPanel.setBorder(new TitledBorder("Período"));
        chkTodas = new JCheckBox("Todas as transações", true);
        periodoPanel.add(chkTodas);
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            txtDataInicio = new JFormattedTextField(mask);
            txtDataFim    = new JFormattedTextField(mask);
        } catch (Exception e) {
            txtDataInicio = new JFormattedTextField();
            txtDataFim    = new JFormattedTextField();
        }
        txtDataInicio.setColumns(8);
        txtDataInicio.setEnabled(false);
        txtDataFim.setColumns(8);
        txtDataFim.setEnabled(false);
        periodoPanel.add(new JLabel("De:"));
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(new JLabel("Até:"));
        periodoPanel.add(txtDataFim);
        filtroPanel.add(periodoPanel);

        chkTodas.addItemListener(e -> {
            boolean sel = (e.getStateChange() == ItemEvent.SELECTED);
            txtDataInicio.setEnabled(!sel);
            txtDataFim.setEnabled(!sel);
            if (sel) {
                txtDataInicio.setValue(null);
                txtDataFim.setValue(null);
            }
        });

        // Classificações
        JPanel classPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        classPanel.setBorder(new TitledBorder("Classificações"));
        chkReceita = new JCheckBox("Receita", true);
        chkDespesa = new JCheckBox("Despesa", true);
        classPanel.add(chkReceita);
        classPanel.add(chkDespesa);
        filtroPanel.add(classPanel);

        // Categorias
        JPanel catPanel = new JPanel(new BorderLayout());
        catPanel.setBorder(new TitledBorder("Categorias (selecione múltiplas)"));
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        chkTodasCats = new JCheckBox("Todas", true);
        boxPanel.add(chkTodasCats);
        chkCategorias = new ArrayList<>();

        CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
        List<Categoria> listaCategorias = categoriaDAO.buscarPorUsuario(usuario.getLogin());
        for (Categoria categoria : listaCategorias) {
            String nomeCat = categoria.getNome();
            JCheckBox cb = new JCheckBox(nomeCat, true);
            chkCategorias.add(cb);
            boxPanel.add(cb);
        }
        chkTodasCats.addActionListener(e ->
                chkCategorias.forEach(cb -> cb.setSelected(chkTodasCats.isSelected()))
        );
        chkCategorias.forEach(cb ->
                cb.addActionListener(e -> {
                    if (!cb.isSelected()) {
                        chkTodasCats.setSelected(false);
                    } else if (chkCategorias.stream().allMatch(JCheckBox::isSelected)) {
                        chkTodasCats.setSelected(true);
                    }
                })
        );

        JScrollPane catScroll = new JScrollPane(boxPanel);
        catScroll.setPreferredSize(new Dimension(250, 120));
        catPanel.add(catScroll, BorderLayout.CENTER);
        filtroPanel.add(catPanel);

        // Filtros
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnFiltrar       = new JButton("Filtrar");    btnFiltrar.setPreferredSize(new Dimension(100,30));
        btnAtualizar     = new JButton("Atualizar");  btnAtualizar.setPreferredSize(new Dimension(100,30));
        btnVoltarFiltros = new JButton("Voltar");     btnVoltarFiltros.setPreferredSize(new Dimension(100,30));
        btnPanel.add(btnFiltrar);
        btnPanel.add(btnAtualizar);
        btnPanel.add(btnVoltarFiltros);
        filtroPanel.add(btnPanel);

        btnFiltrar.addActionListener(e -> {
            aplicarFiltro();
            tabPane.setSelectedIndex(0);
        });

        btnAtualizar.addActionListener(e -> {
            chkTodas.setSelected(true);
            chkReceita.setSelected(true);
            chkDespesa.setSelected(true);
            chkTodasCats.setSelected(true);
            chkCategorias.forEach(cb -> cb.setSelected(true));

            todasTransacoes = transacaoDAO.buscarPorUsuario(usuario.getLogin());
            carregarTabela(todasTransacoes, null, null);
        });

        btnVoltarFiltros.addActionListener(e -> {
            new MainView(usuario).setVisible(true);
            dispose();
        });

        tabPane.addTab("Transações", panelTransacoes);
        tabPane.addTab("Filtros", filtroPanel);
        add(tabPane);
    }

    private void carregarTabela(List<Transacao> transacoes, LocalDate inicio, LocalDate fim) {
        tableModel.setRowCount(0);
        double somaR = 0, somaD = 0;

        for (Transacao t : transacoes) {
            String dataStr = DATE_FMT.format(t.getData());
            String desc    = t.getDescricao();
            String valStr  = String.format("R$ %.2f", t.getValor());
            String clas    = t.getClassificacao();
            String cat     = t.getCategoria().getNome();

            tableModel.addRow(new Object[]{ dataStr, desc, valStr, clas, cat, "Editar", "Excluir" });

            if ("Receita".equalsIgnoreCase(clas)) somaR += t.getValor();
            else somaD += t.getValor();
        }

        String label = (inicio == null || fim == null)
                ? "(total)"
                : String.format("(%s - %s)", DATE_FMT.format(inicio), DATE_FMT.format(fim));
        lblResumo.setText(
                String.format("Período: %s - Total Receitas: R$ %.2f   Total Despesas: R$ %.2f",
                        label, somaR, somaD)
        );
    }

    static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable tbl, Object val, boolean sel, boolean focus, int row, int col) {
            setText(val != null ? val.toString() : "");
            setSize(tbl.getColumnModel().getColumn(col).getWidth(), getPreferredSize().height);
            setBackground(sel ? tbl.getSelectionBackground() : tbl.getBackground());
            setForeground(sel ? tbl.getSelectionForeground() : tbl.getForeground());
            int h = getPreferredSize().height;
            if (tbl.getRowHeight(row) != h) tbl.setRowHeight(row, h);
            return this;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton  button;
        private String   label;
        private boolean  isPushed;
        private int      rowAtivo;
        private int      colAtivo;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            label    = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            rowAtivo = row;
            colAtivo = column;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                Transacao transacao = todasTransacoes.get(rowAtivo);

                if (colAtivo == COL_EXCLUIR) {
                    int confirm = JOptionPane.showConfirmDialog(
                            VisualizarTransacaoView.this,
                            "Deseja realmente excluir esta transação?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        transacaoDAO.deletar(transacao);
                        SwingUtilities.invokeLater(() -> {
                            todasTransacoes = transacaoDAO.buscarPorUsuario(usuario.getLogin());
                            carregarTabela(todasTransacoes, null, null);
                        });
                    }
                }
                else if (colAtivo == COL_EDITAR) {
                    SwingUtilities.invokeLater(() -> {
                        editarTransacao(transacao);
                    });
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private void editarTransacao(Transacao t) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;
        // Data
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        JFormattedTextField txtData = null;
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mask);
        } catch (Exception ex) {
            txtData = new JFormattedTextField();
        }
        txtData.setColumns(8);
        txtData.setText(DATE_FMT.format(t.getData()));
        gbc.gridx = 1;
        panel.add(txtData, gbc);

        // Valor
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Valor (R$):"), gbc);
        JTextField txtValor = new JTextField(String.format("%.2f", t.getValor()), 10);
        gbc.gridx = 1;
        panel.add(txtValor, gbc);

        // Classificação
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Classificação:"), gbc);
        JComboBox<String> cbClass = new JComboBox<>(new String[]{"Receita", "Despesa"});
        cbClass.setSelectedItem(t.getClassificacao());
        gbc.gridx = 1;
        panel.add(cbClass, gbc);

        // Categoria
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Categoria:"), gbc);
        CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
        List<Categoria> listaCats = categoriaDAO.buscarPorUsuario(usuario.getLogin());
        String[] nomesCats = listaCats.stream().map(Categoria::getNome).toArray(String[]::new);
        JComboBox<String> cbCat = new JComboBox<>(nomesCats);
        cbCat.setSelectedItem(t.getCategoria().getNome());
        gbc.gridx = 1;
        panel.add(cbCat, gbc);

        // Descrição
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Descrição:"), gbc);
        JTextField txtDesc = new JTextField(t.getDescricao(), 15);
        gbc.gridx = 1;
        panel.add(txtDesc, gbc);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Editar Transação",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate novaData = LocalDate.parse(txtData.getText(), DATE_FMT);

                float novoValor = Float.parseFloat(txtValor.getText().replace(',', '.'));

                String novaClas = (String) cbClass.getSelectedItem();

                String novoNomeCat = (String) cbCat.getSelectedItem();
                Categoria novaCatEnt = categoriaDAO.buscarPorNomeEUsuario(novoNomeCat, usuario.getLogin());
                if (novaCatEnt == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Categoria selecionada não encontrada.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                String novaDesc = txtDesc.getText().trim();
                if (novaDesc.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Descrição não pode ficar vazia.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                t.setData(novaData);
                t.setValor(novoValor);
                t.setClassificacao(novaClas);
                t.setCategoria(novaCatEnt);
                t.setDescricao(novaDesc);
                transacaoDAO.atualizar(t);

                todasTransacoes = transacaoDAO.buscarPorUsuario(usuario.getLogin());
                carregarTabela(todasTransacoes, null, null);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Valor inválido. Use apenas números.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Data inválida. Use dd/MM/yyyy.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void aplicarFiltro() {
        try {
            LocalDate inicio = chkTodas.isSelected()
                    ? null
                    : LocalDate.parse(txtDataInicio.getText(), DATE_FMT);
            LocalDate fim = chkTodas.isSelected()
                    ? null
                    : LocalDate.parse(txtDataFim.getText(), DATE_FMT);

            boolean filRec = chkReceita.isSelected();
            boolean filDes = chkDespesa.isSelected();
            boolean todasCats = chkTodasCats.isSelected();

            List<String> selCats = todasCats
                    ? Collections.emptyList()
                    : chkCategorias.stream()
                    .filter(JCheckBox::isSelected)
                    .map(AbstractButton::getText)
                    .collect(Collectors.toList());

            List<Transacao> filtradas = todasTransacoes.stream()
                    .filter(t -> {
                        if (inicio == null) return true;
                        LocalDate d = t.getData();
                        return !d.isBefore(inicio) && !d.isAfter(fim);
                    })
                    .filter(t -> {
                        boolean okClas = (filRec && "Receita".equalsIgnoreCase(t.getClassificacao()))
                                || (filDes && "Despesa".equalsIgnoreCase(t.getClassificacao()));
                        boolean okCat = todasCats || selCats.contains(t.getCategoria().getNome());
                        return okClas && okCat;
                    })
                    .collect(Collectors.toList());

            carregarTabela(filtradas, inicio, fim);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data inválida. Use dd/MM/yyyy.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

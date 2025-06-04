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
        setSize(650, 400);
        setLocationRelativeTo(null);

        tabPane = new JTabbedPane();

        // Aba Transações
        JPanel panelTransacoes = new JPanel(new BorderLayout(10, 10));
        String[] cols = {"Data", "Descrição", "Valor", "Classificação", "Categoria"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
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
                    if (!cb.isSelected())
                        chkTodasCats.setSelected(false);
                    else if (chkCategorias.stream().allMatch(JCheckBox::isSelected))
                        chkTodasCats.setSelected(true);
                })
        );

        JScrollPane catScroll = new JScrollPane(boxPanel);
        catScroll.setPreferredSize(new Dimension(250, 120));
        catPanel.add(catScroll, BorderLayout.CENTER);
        filtroPanel.add(catPanel);

        // Botões Filtros
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
            String desc = t.getDescricao();
            String valStr = String.format("R$ %.2f", t.getValor());
            String clas = t.getClassificacao();
            String cat  = t.getCategoria().getNome();

            tableModel.addRow(new Object[]{ dataStr, desc, valStr, clas, cat });

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
        public Component getTableCellRendererComponent(JTable tbl, Object val, boolean sel, boolean focus, int row, int col) {
            setText(val != null ? val.toString() : "");
            setSize(tbl.getColumnModel().getColumn(col).getWidth(), getPreferredSize().height);
            setBackground(sel ? tbl.getSelectionBackground() : tbl.getBackground());
            setForeground(sel ? tbl.getSelectionForeground() : tbl.getForeground());
            int h = getPreferredSize().height;
            if (tbl.getRowHeight(row) != h) tbl.setRowHeight(row, h);
            return this;
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

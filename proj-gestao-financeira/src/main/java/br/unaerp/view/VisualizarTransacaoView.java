package br.unaerp.view;

import br.unaerp.model.Transacao;
import br.unaerp.model.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
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
    private JTable table;
    private DefaultTableModel tableModel;

    private JFormattedTextField txtDataInicio;
    private JFormattedTextField txtDataFim;
    private JCheckBox chkTodas, chkReceita, chkDespesa;
    private JCheckBox chkTodasCats;
    private List<JCheckBox> chkCategorias;
    private JButton btnFiltrar, btnAtualizar, btnVoltarFiltros, btnVoltarTransacoes;

    private JLabel lblTotalReceitas;
    private JLabel lblTotalDespesas;

    private JTabbedPane tabPane;

    public VisualizarTransacaoView(Usuario usuario) {
        super("Visualizar Transações");
        this.usuario = usuario;
        initComponents();
        carregarTabela(usuario.getTransacoes(), null, null);
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        tabPane = new JTabbedPane();

        // === Aba Transações ===
        JPanel panelTransacoes = new JPanel(new BorderLayout(10, 10));
        String[] cols = {"Data", "Descrição", "Valor", "Classificação", "Categoria"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        panelTransacoes.add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de totais
        JPanel totalPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        lblTotalReceitas = new JLabel("Total Receitas: R$ 0.00", SwingConstants.RIGHT);
        lblTotalDespesas = new JLabel("Total Despesas: R$ 0.00", SwingConstants.RIGHT);
        totalPanel.add(lblTotalReceitas);
        totalPanel.add(lblTotalDespesas);

        // Botão Voltar na aba Transações
        btnVoltarTransacoes = new JButton("Voltar");
        btnVoltarTransacoes.addActionListener(e -> {
            new MainView(usuario).setVisible(true);
            dispose();
        });
        JPanel backPanelTrans = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanelTrans.add(btnVoltarTransacoes);

        // Combinando totais + voltar
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(totalPanel, BorderLayout.NORTH);
        southContainer.add(backPanelTrans, BorderLayout.SOUTH);
        panelTransacoes.add(southContainer, BorderLayout.SOUTH);


        // === Aba Filtros ===
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
        txtDataFim   .setColumns(8);
        txtDataInicio.setEnabled(false);
        txtDataFim   .setEnabled(false);
        periodoPanel.add(new JLabel("De:"));
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(new JLabel("Até:"));
        periodoPanel.add(txtDataFim);
        filtroPanel.add(periodoPanel);

        chkTodas.addItemListener(e -> {
            boolean sel = (e.getStateChange() == ItemEvent.SELECTED);
            txtDataInicio.setEnabled(!sel);
            txtDataFim   .setEnabled(!sel);
            if (sel) {
                txtDataInicio.setValue(null);
                txtDataFim   .setValue(null);
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

        // Categorias (checkboxes)
        JPanel catPanel = new JPanel(new BorderLayout());
        catPanel.setBorder(new TitledBorder("Categorias (selecione múltiplas)"));
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        chkTodasCats = new JCheckBox("Todas", true);
        boxPanel.add(chkTodasCats);

        chkCategorias = new ArrayList<>();
        for (String cat : usuario.getCategoria().getCategorias()) {
            JCheckBox cb = new JCheckBox(cat, true);
            chkCategorias.add(cb);
            boxPanel.add(cb);
        }

        chkTodasCats.addActionListener(e -> {
            boolean tudo = chkTodasCats.isSelected();
            chkCategorias.forEach(cb -> cb.setSelected(tudo));
        });
        chkCategorias.forEach(cb -> cb.addActionListener(e -> {
            if (!cb.isSelected()) {
                chkTodasCats.setSelected(false);
            } else {
                boolean all = chkCategorias.stream().allMatch(JCheckBox::isSelected);
                if (all) chkTodasCats.setSelected(true);
            }
        }));

        JScrollPane catScroll = new JScrollPane(boxPanel);
        catScroll.setPreferredSize(new Dimension(250, 120));
        catPanel.add(catScroll, BorderLayout.CENTER);
        filtroPanel.add(catPanel);

        // Botões da aba Filtros
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnFiltrar       = new JButton("Filtrar");
        btnAtualizar     = new JButton("Atualizar");
        btnVoltarFiltros = new JButton("Voltar");
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
            carregarTabela(usuario.getTransacoes(), null, null);
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
            tableModel.addRow(new Object[]{
                    DATE_FMT.format(LocalDate.of(t.getAno(), t.getMes(), t.getDia())),
                    t.getDescricao(),
                    String.format("R$ %.2f", t.getValor()),
                    t.getClassificacao(),
                    t.getCategoria()
            });
            if ("Receita".equalsIgnoreCase(t.getClassificacao())) somaR += t.getValor();
            else somaD += t.getValor();
        }
        String label = (inicio == null || fim == null)
                ? "(período total)"
                : String.format("(%s - %s)", DATE_FMT.format(inicio), DATE_FMT.format(fim));
        lblTotalReceitas.setText(String.format("Total Receitas %s: R$ %.2f", label, somaR));
        lblTotalDespesas .setText(String.format("Total Despesas %s: R$ %.2f", label, somaD));
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

            List<Transacao> filtradas = usuario.getTransacoes().stream()
                    .filter(t -> {
                        if (inicio == null) return true;
                        LocalDate d = LocalDate.of(t.getAno(), t.getMes(), t.getDia());
                        return !d.isBefore(inicio) && !d.isAfter(fim);
                    })
                    .filter(t -> {
                        boolean okClass = (filRec && "Receita".equalsIgnoreCase(t.getClassificacao()))
                                || (filDes && "Despesa".equalsIgnoreCase(t.getClassificacao()));
                        boolean okCat   = todasCats || selCats.contains(t.getCategoria());
                        return okClass && okCat;
                    })
                    .collect(Collectors.toList());

            carregarTabela(filtradas, inicio, fim);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Data inválida. Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

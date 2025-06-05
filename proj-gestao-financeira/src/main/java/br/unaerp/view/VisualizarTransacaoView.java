package br.unaerp.view;

import br.unaerp.model.Categoria;
import br.unaerp.model.Transacao;
import br.unaerp.model.Usuario;
import br.unaerp.model.DAO.CategoriaDAOImpl;
import br.unaerp.model.DAO.CategoriaDAO;
import br.unaerp.model.DAO.TransacaoDAOImpl;
import br.unaerp.model.DAO.TransacaoDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JLabel lblResumo;
    private JButton btnVoltarTransacoes;

    private JFormattedTextField txtDataInicio;
    private JFormattedTextField txtDataFim;
    private JCheckBox chkTodasDatas;
    private JCheckBox chkReceita, chkDespesa;
    private JCheckBox chkTodasCategorias;
    private List<JCheckBox> chkCategorias;
    private JButton btnFiltrar, btnLimpar, btnVoltarFiltros;

    private JTabbedPane tabPane;

    private JPanel panelCategoriasBox;

    private static final int COL_EDITAR = 5;
    private static final int COL_EXCLUIR = 6;

    private List<Transacao> listaTransacoesAtuais = new ArrayList<>();

    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    public VisualizarTransacaoView(Usuario usuario) {
        super("Visualizar Transações");
        this.usuario = usuario;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);

        tabPane = new JTabbedPane();

        JPanel panelTransacoes = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Data", "Descrição", "Valor", "Classificação", "Categoria", "Editar", "Excluir"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == COL_EDITAR || col == COL_EXCLUIR);
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer truncRenderer = new DefaultTableCellRenderer();
        truncRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        table.getColumnModel().getColumn(1).setCellRenderer(truncRenderer);

        table.getColumnModel().getColumn(COL_EDITAR).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_EDITAR).setMaxWidth(80);
        table.getColumnModel().getColumn(COL_EXCLUIR).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_EXCLUIR).setMaxWidth(80);

        table.getColumn("Editar").setCellRenderer((TableCellRenderer) new ButtonRenderer());
        table.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox()));

        table.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
        table.getColumn("Excluir").setCellEditor(new ButtonEditor(new JCheckBox()));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0 && col != COL_EDITAR && col != COL_EXCLUIR) {
                    Object value = table.getValueAt(row, col);
                    if (value != null) {
                        JTextArea textArea = new JTextArea(value.toString());
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        JScrollPane scroll = new JScrollPane(textArea);
                        scroll.setPreferredSize(new Dimension(400, 200));
                        JOptionPane.showMessageDialog(
                                VisualizarTransacaoView.this,
                                scroll,
                                "Conteúdo Completo",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        panelTransacoes.add(new JScrollPane(table), BorderLayout.CENTER);

        lblResumo = new JLabel(
                "Período: (total)   -   Total Receitas: R$ 0.00   Total Despesas: R$ 0.00",
                SwingConstants.CENTER
        );
        lblResumo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnVoltarTransacoes = new JButton("Voltar");
        btnVoltarTransacoes.setPreferredSize(new Dimension(100, 30));
        JPanel backPanelTrans = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanelTrans.add(btnVoltarTransacoes);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(lblResumo, BorderLayout.NORTH);
        southContainer.add(new JSeparator(), BorderLayout.CENTER);
        southContainer.add(backPanelTrans, BorderLayout.SOUTH);
        panelTransacoes.add(southContainer, BorderLayout.SOUTH);

        JPanel filtroPanel = new JPanel();
        filtroPanel.setLayout(new BoxLayout(filtroPanel, BoxLayout.Y_AXIS));
        filtroPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        periodoPanel.setBorder(new TitledBorder("Período"));

        chkTodasDatas = new JCheckBox("Todas as transações", true);
        periodoPanel.add(chkTodasDatas);

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            txtDataInicio = new JFormattedTextField(mask);
            txtDataFim = new JFormattedTextField(mask);
        } catch (Exception e) {
            txtDataInicio = new JFormattedTextField();
            txtDataFim = new JFormattedTextField();
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

        chkTodasDatas.addActionListener(e -> {
            boolean selecionado = chkTodasDatas.isSelected();
            txtDataInicio.setEnabled(!selecionado);
            txtDataFim.setEnabled(!selecionado);
            if (selecionado) {
                txtDataInicio.setValue(null);
                txtDataFim.setValue(null);
            }
        });

        JPanel classPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        classPanel.setBorder(new TitledBorder("Classificações"));
        chkReceita = new JCheckBox("Receita", true);
        chkDespesa = new JCheckBox("Despesa", true);
        classPanel.add(chkReceita);
        classPanel.add(chkDespesa);
        filtroPanel.add(classPanel);

        JPanel catPanel = new JPanel(new BorderLayout());
        catPanel.setBorder(new TitledBorder("Categorias (selecione múltiplas)"));

        panelCategoriasBox = new JPanel();
        panelCategoriasBox.setLayout(new BoxLayout(panelCategoriasBox, BoxLayout.Y_AXIS));

        chkTodasCategorias = new JCheckBox("Todas", true);
        panelCategoriasBox.add(chkTodasCategorias);

        chkCategorias = new ArrayList<>();

        JScrollPane categoriaScroll = new JScrollPane(panelCategoriasBox);
        categoriaScroll.setPreferredSize(new Dimension(250, 120));
        catPanel.add(categoriaScroll, BorderLayout.CENTER);
        filtroPanel.add(catPanel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setPreferredSize(new Dimension(100, 30));
        btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(100, 30));
        btnVoltarFiltros = new JButton("Voltar");
        btnVoltarFiltros.setPreferredSize(new Dimension(100, 30));

        btnPanel.add(btnFiltrar);
        btnPanel.add(btnLimpar);
        btnPanel.add(btnVoltarFiltros);
        filtroPanel.add(btnPanel);

        tabPane.addTab("Transações", panelTransacoes);
        tabPane.addTab("Filtros", filtroPanel);

        add(tabPane);
    }

    public void setListaTransacoes(List<Transacao> transacoes) {
        listaTransacoesAtuais = transacoes;
        tableModel.setRowCount(0);

        double somaReceita = 0, somaDespesa = 0;
        for (Transacao t : transacoes) {
            String dataStr = DATE_FMT.format(t.getData());
            String desc = t.getDescricao();
            String valStr = String.format("R$ %.2f", t.getValor());
            String clas = t.getClassificacao();
            String cat = t.getCategoria().getNome();

            tableModel.addRow(new Object[]{dataStr, desc, valStr, clas, cat, "Editar", "Excluir"});
            if ("Receita".equalsIgnoreCase(clas)) somaReceita += t.getValor();
            else somaDespesa += t.getValor();
        }

        lblResumo.setText(String.format(
                "Período: (total)   -   Total Receitas: R$ %.2f   Total Despesas: R$ %.2f",
                somaReceita, somaDespesa));
    }

    public void setListaTransacoesFiltradas(List<Transacao> transacoes,
                                            java.time.LocalDate dataIni,
                                            java.time.LocalDate dataFim) {
        listaTransacoesAtuais = transacoes;
        tableModel.setRowCount(0);

        double somaReceita = 0, somaDespesa = 0;
        for (Transacao t : transacoes) {
            String dataStr = DATE_FMT.format(t.getData());
            String desc = t.getDescricao();
            String valStr = String.format("R$ %.2f", t.getValor());
            String clas = t.getClassificacao();
            String cat = t.getCategoria().getNome();

            tableModel.addRow(new Object[]{dataStr, desc, valStr, clas, cat, "Editar", "Excluir"});
            if ("Receita".equalsIgnoreCase(clas)) somaReceita += t.getValor();
            else somaDespesa += t.getValor();
        }

        String labelPeriodo = "(total)";
        if (dataIni != null && dataFim != null) {
            labelPeriodo = String.format("(%s - %s)", DATE_FMT.format(dataIni), DATE_FMT.format(dataFim));
        }
        lblResumo.setText(String.format(
                "Período: %s   -   Total Receitas: R$ %.2f   Total Despesas: R$ %.2f",
                labelPeriodo, somaReceita, somaDespesa));
    }

    public void setListaCategoriasFiltro(List<Categoria> listaCategorias) {
        panelCategoriasBox.removeAll();
        panelCategoriasBox.add(chkTodasCategorias);
        chkCategorias.clear();

        for (Categoria categoria : listaCategorias) {
            JCheckBox checkBox = new JCheckBox(categoria.getNome(), true);
            chkCategorias.add(checkBox);
            panelCategoriasBox.add(checkBox);

            checkBox.addActionListener(e -> {
                if (!checkBox.isSelected()) {
                    chkTodasCategorias.setSelected(false);
                } else {
                    boolean todasSel = chkCategorias.stream().allMatch(JCheckBox::isSelected);
                    chkTodasCategorias.setSelected(todasSel);
                }
            });
        }

        chkTodasCategorias.addActionListener(e -> {
            boolean seleciona = chkTodasCategorias.isSelected();
            chkCategorias.forEach(checkBox -> checkBox.setSelected(seleciona));
        });

        panelCategoriasBox.revalidate();
        panelCategoriasBox.repaint();
    }

    public Transacao getTransacaoPorLinha(int linha) {
        if (linha < 0 || linha >= listaTransacoesAtuais.size()) return null;
        return listaTransacoesAtuais.get(linha);
    }

    public JTable getTable() {
        return table;
    }

    public boolean isTodasDatasSelecionado() {
        return chkTodasDatas.isSelected();
    }

    public String getDataInicio() {
        return txtDataInicio.getText().trim();
    }

    public String getDataFim() {
        return txtDataFim.getText().trim();
    }

    public boolean isReceitaSelecionado() {
        return chkReceita.isSelected();
    }

    public boolean isDespesaSelecionado() {
        return chkDespesa.isSelected();
    }

    public boolean isTodasCategoriasSelecionado() {
        return chkTodasCategorias.isSelected();
    }

    public List<String> getCategoriasSelecionadas() {
        return chkCategorias.stream()
                .filter(JCheckBox::isSelected)
                .map(JCheckBox::getText)
                .collect(Collectors.toList());
    }

    public void addFiltrarListener(ActionListener actionListener) {
        btnFiltrar.addActionListener(actionListener);
    }

    public void addLimparListener(ActionListener actionListener) {
        btnLimpar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    VisualizarTransacaoView.this,
                    "Realmente deseja limpar os filtros?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                actionListener.actionPerformed(e);
            }
        });
    }

    public void addVoltarFiltrosListener(ActionListener actionListener) {
        btnVoltarFiltros.addActionListener(actionListener);
    }

    public void addVoltarTransacoesListener(ActionListener actionListener) {
        btnVoltarTransacoes.addActionListener(actionListener);
    }

    public void selecionarAbaTransacoes() {
        tabPane.setSelectedIndex(0);
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
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
        private final JButton button;
        private boolean isPushed;
        private int rowAtivo;
        private int colAtivo;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            isPushed = true;
            rowAtivo = row;
            colAtivo = column;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                Transacao transacao = getTransacaoPorLinha(rowAtivo);
                if (transacao != null) {
                    if (colAtivo == COL_EXCLUIR) {
                        int confirm = JOptionPane.showConfirmDialog(
                                VisualizarTransacaoView.this,
                                "Deseja realmente excluir esta transação?",
                                "Confirmação",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            transacaoDAO.deletar(transacao);
                            List<Transacao> atualizadas = transacaoDAO.buscarPorUsuario(usuario.getLogin());
                            setListaTransacoes(atualizadas);
                        }
                    } else if (colAtivo == COL_EDITAR) {
                        editarTransacao(transacao);
                    }
                }
            }
            isPushed = false;
            return button.getText();
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

    private void editarTransacao(Transacao transacao) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        gbc.gridx = 0;
        gbc.gridy = y;
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
        txtData.setText(DATE_FMT.format(transacao.getData()));
        gbc.gridx = 1;
        panel.add(txtData, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Valor (R$):"), gbc);
        JTextField txtValor = new JTextField(String.format("%.2f", transacao.getValor()), 10);
        gbc.gridx = 1;
        panel.add(txtValor, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Classificação:"), gbc);
        JComboBox<String> cbClass = new JComboBox<>(new String[]{"Receita", "Despesa"});
        cbClass.setSelectedItem(transacao.getClassificacao());
        gbc.gridx = 1;
        panel.add(cbClass, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Categoria:"), gbc);
        List<Categoria> listaCats = categoriaDAO.buscarPorUsuario(usuario.getLogin());
        String[] nomesCats = listaCats.stream().map(Categoria::getNome).toArray(String[]::new);
        JComboBox<String> cbCat = new JComboBox<>(nomesCats);
        cbCat.setSelectedItem(transacao.getCategoria().getNome());
        gbc.gridx = 1;
        panel.add(cbCat, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Descrição:"), gbc);
        JTextField txtDesc = new JTextField(transacao.getDescricao(), 15);
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

                transacao.setData(novaData);
                transacao.setValor(novoValor);
                transacao.setClassificacao(novaClas);
                transacao.setCategoria(novaCatEnt);
                transacao.setDescricao(novaDesc);
                transacaoDAO.atualizar(transacao);

                List<Transacao> atualizadas = transacaoDAO.buscarPorUsuario(usuario.getLogin());
                setListaTransacoes(atualizadas);

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
}

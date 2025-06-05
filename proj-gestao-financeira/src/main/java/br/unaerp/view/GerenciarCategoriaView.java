package br.unaerp.view;

import br.unaerp.model.Categoria;
import br.unaerp.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class GerenciarCategoriaView extends JFrame {

    private final Usuario usuario;

    private JTabbedPane tabPane;

    private JTextField txtNovaCategoria;
    private JButton btnAddCategoria;

    private JComboBox<Categoria> cbEditarCategoria;
    private JTextField txtEditarCategoria;
    private JButton btnSalvarEdicao;

    private JComboBox<Categoria> cbExcluirCategoria;
    private JButton btnExcluirCategoria;

    private JButton btnVoltar;

    public GerenciarCategoriaView(Usuario usuario) {
        super("Gerenciar Categorias");
        this.usuario = usuario;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Gerenciamento de Categorias");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // Abas
        tabPane = new JTabbedPane();
        tabPane.addTab("Adicionar", criarPainelAdicionar());
        tabPane.addTab("Editar", criarPainelEditar());
        tabPane.addTab("Excluir", criarPainelExcluir());
        add(tabPane, BorderLayout.CENTER);

        // Botão Voltar
        btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(100, 30));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(btnVoltar);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel criarPainelAdicionar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome da nova categoria:"), gbc);

        gbc.gridx = 1;
        txtNovaCategoria = new JTextField(20);
        txtNovaCategoria.setToolTipText("Digite o nome da categoria a ser adicionada");
        panel.add(txtNovaCategoria, gbc);

        // Adicionar
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        btnAddCategoria = new JButton("Adicionar");
        btnAddCategoria.setPreferredSize(new Dimension(140, 30));
        panel.add(btnAddCategoria, gbc);

        return panel;
    }

    // Editar Categoria
    private JPanel criarPainelEditar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Selecione categoria:"), gbc);

        gbc.gridx = 1;
        cbEditarCategoria = new JComboBox<>();
        cbEditarCategoria.setPreferredSize(new Dimension(200, 30));
        panel.add(cbEditarCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Novo nome:"), gbc);

        gbc.gridx = 1;
        txtEditarCategoria = new JTextField(20);
        txtEditarCategoria.setToolTipText("Digite o novo nome para a categoria selecionada");
        panel.add(txtEditarCategoria, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        btnSalvarEdicao = new JButton("Salvar Alteração");
        btnSalvarEdicao.setPreferredSize(new Dimension(140, 30));
        panel.add(btnSalvarEdicao, gbc);

        return panel;
    }

    // Excluir Categoria
    private JPanel criarPainelExcluir() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Selecione categoria:"), gbc);

        gbc.gridx = 1;
        cbExcluirCategoria = new JComboBox<>();
        cbExcluirCategoria.setPreferredSize(new Dimension(200, 30));
        panel.add(cbExcluirCategoria, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        btnExcluirCategoria = new JButton("Excluir");
        btnExcluirCategoria.setPreferredSize(new Dimension(140, 30));
        panel.add(btnExcluirCategoria, gbc);

        return panel;
    }

    public void addAdicionarListener(ActionListener listener) {
        btnAddCategoria.addActionListener(listener);
    }

    public void addSalvarAlteracaoListener(ActionListener listener) {
        btnSalvarEdicao.addActionListener(listener);
    }

    public void addExcluirListener(ActionListener listener) {
        btnExcluirCategoria.addActionListener(listener);
    }

    public void addVoltarListener(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }

    public void clearCampos() {
        txtNovaCategoria.setText("");
        txtEditarCategoria.setText("");
    }

    public String getNomeNovaCategoria() {
        return txtNovaCategoria.getText().trim();
    }

    public Categoria getCategoriaSelecionadaParaEditar() {
        return (Categoria) cbEditarCategoria.getSelectedItem();
    }

    public String getNovoNomeCategoria() {
        return txtEditarCategoria.getText().trim();
    }

    public Categoria getCategoriaSelecionadaParaExcluir() {
        return (Categoria) cbExcluirCategoria.getSelectedItem();
    }

    public void setListaCategoriasParaEditar(List<Categoria> listaCategorias) {
        DefaultComboBoxModel<Categoria> model = new DefaultComboBoxModel<>();
        for (Categoria c : listaCategorias) {
            model.addElement(c);
        }
        cbEditarCategoria.setModel(model);

        cbEditarCategoria.setRenderer(new DefaultListCellRenderer() {
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

    public void setListaCategoriasParaExcluir(List<Categoria> listaCategorias) {
        DefaultComboBoxModel<Categoria> model = new DefaultComboBoxModel<>();
        for (Categoria c : listaCategorias) {
            model.addElement(c);
        }
        cbExcluirCategoria.setModel(model);

        cbExcluirCategoria.setRenderer(new DefaultListCellRenderer() {
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
}

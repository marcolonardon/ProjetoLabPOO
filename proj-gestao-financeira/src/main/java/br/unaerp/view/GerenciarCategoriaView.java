package br.unaerp.view;

import br.unaerp.model.Categoria;
import br.unaerp.model.DAO.CategoriaDAO;
import br.unaerp.model.DAO.CategoriaDAOImpl;
import br.unaerp.model.Transacao;
import br.unaerp.model.DAO.TransacaoDAO;
import br.unaerp.model.DAO.TransacaoDAOImpl;
import br.unaerp.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciarCategoriaView extends JFrame {

    private final Usuario usuario;
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();

    private JButton btnVoltar;
    private JTextField txtNovaCategoria;
    private JTextField txtEditarCategoria;
    private JComboBox<String> cbEditarCategoria;
    private JComboBox<String> cbExcluirCategoria;

    public GerenciarCategoriaView(Usuario usuario) {
        super("Gerenciar Categorias");
        this.usuario = usuario;
        initComponents();
        carregarCombos();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Gerenciamento de Categorias");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // Abas
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Adicionar", criarPainelAdicionar());
        tabPane.addTab("Editar", criarPainelEditar());
        tabPane.addTab("Excluir", criarPainelExcluir());
        add(tabPane, BorderLayout.CENTER);

        // Botão Voltar
        btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(100, 30));
        btnVoltar.addActionListener(e -> {
            new MainView(usuario).setVisible(true);
            dispose();
        });
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(btnVoltar);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel criarPainelAdicionar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // nova categoria
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome da nova categoria:"), gbc);

        gbc.gridx = 1;
        txtNovaCategoria = new JTextField(20);
        txtNovaCategoria.setToolTipText("Digite o nome da categoria a ser adicionada");
        panel.add(txtNovaCategoria, gbc);

        // Botão Adicionar
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnAdd = new JButton("Adicionar");
        btnAdd.setPreferredSize(new Dimension(140, 30));
        btnAdd.addActionListener(e -> {
            String nome = txtNovaCategoria.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "O nome da categoria não pode ser vazio.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Categoria existente = categoriaDAO.buscarPorNomeEUsuario(nome, usuario.getLogin());
            if (existente != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Categoria já existe para este usuário.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Categoria nova = new Categoria(nome, usuario);
            categoriaDAO.salvar(nova);
            JOptionPane.showMessageDialog(
                    this,
                    "Categoria adicionada com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            txtNovaCategoria.setText("");
            carregarCombos();
        });
        panel.add(btnAdd, gbc);

        return panel;
    }

    private JPanel criarPainelEditar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Combo categoria
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Selecione categoria:"), gbc);

        gbc.gridx = 1;
        cbEditarCategoria = new JComboBox<>();
        cbEditarCategoria.setPreferredSize(new Dimension(200, 30));
        panel.add(cbEditarCategoria, gbc);

        // novo nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Novo nome:"), gbc);

        gbc.gridx = 1;
        txtEditarCategoria = new JTextField(20);
        txtEditarCategoria.setToolTipText("Digite o novo nome para a categoria selecionada");
        panel.add(txtEditarCategoria, gbc);

        // Salvar Alteração
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnEdit = new JButton("Salvar Alteração");
        btnEdit.setPreferredSize(new Dimension(140, 30));
        btnEdit.addActionListener(e -> {
            String atual = (String) cbEditarCategoria.getSelectedItem();
            String novoNome = txtEditarCategoria.getText().trim();

            if (atual == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não há categorias para editar.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "O novo nome não pode ser vazio.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Categoria categoria = categoriaDAO.buscarPorNomeEUsuario(atual, usuario.getLogin());
            if (categoria == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Categoria não encontrada no banco.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                carregarCombos();
                return;
            }

            Categoria jaExiste = categoriaDAO.buscarPorNomeEUsuario(novoNome, usuario.getLogin());
            if (jaExiste != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Já existe uma categoria com este novo nome.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            categoria.setNome(novoNome);
            categoriaDAO.atualizar(categoria);
            JOptionPane.showMessageDialog(
                    this,
                    "Categoria editada com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            txtEditarCategoria.setText("");
            carregarCombos();
        });
        panel.add(btnEdit, gbc);

        return panel;
    }

    private JPanel criarPainelExcluir() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // combo excluir
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Selecione categoria:"), gbc);

        gbc.gridx = 1;
        cbExcluirCategoria = new JComboBox<>();
        cbExcluirCategoria.setPreferredSize(new Dimension(200, 30));
        panel.add(cbExcluirCategoria, gbc);

        // Botão Excluir
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnDel = new JButton("Excluir");
        btnDel.setPreferredSize(new Dimension(140, 30));
        btnDel.addActionListener(e -> {
            String alvo = (String) cbExcluirCategoria.getSelectedItem();
            if (alvo == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não há categorias para excluir.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Categoria cat = categoriaDAO.buscarPorNomeEUsuario(alvo, usuario.getLogin());
            if (cat == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Categoria não encontrada no banco.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                carregarCombos();
                return;
            }

            List<Transacao> transacaoUsando = transacaoDAO.buscarPorUsuario(usuario.getLogin())
                    .stream()
                    .filter(transacao -> transacao.getCategoria().getId().equals(cat.getId()))
                    .collect(Collectors.toList());

            if (!transacaoUsando.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não é possível excluir a categoria \"" + alvo + "\" porque existem transações associadas a ela.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int resp = JOptionPane.showConfirmDialog(
                    this,
                    "Tem certeza que deseja excluir a categoria '" + alvo + "'?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION
            );
            if (resp == JOptionPane.YES_OPTION) {
                categoriaDAO.deletar(cat);
                JOptionPane.showMessageDialog(
                        this,
                        "Categoria excluída com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );
                carregarCombos();
            }
        });
        panel.add(btnDel, gbc);

        return panel;
    }

    private void carregarCombos() {
        List<Categoria> lista = categoriaDAO.buscarPorUsuario(usuario.getLogin());
        List<String> nomes = lista.stream()
                .map(Categoria::getNome)
                .collect(Collectors.toList());
        String[] arrayNomes = nomes.toArray(new String[0]);

        DefaultComboBoxModel<String> modelEdit = new DefaultComboBoxModel<>(arrayNomes);
        cbEditarCategoria.setModel(modelEdit);

        DefaultComboBoxModel<String> modelDel = new DefaultComboBoxModel<>(arrayNomes);
        cbExcluirCategoria.setModel(modelDel);
    }
}

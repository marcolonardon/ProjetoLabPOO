package br.unaerp.view;

import br.unaerp.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class GerenciarCategoriaView extends JFrame {

    private JButton btnVoltar;
    private JTextField txtNovaCategoria;
    private JTextField txtEditarCategoria;
    private JComboBox<String> cbEditarCategoria;
    private JComboBox<String> cbExcluirCategoria;
    private Usuario usuario;

    public GerenciarCategoriaView(Usuario usuario) {
        super("Gerenciar Categorias - Sistema Financeiro");
        this.usuario = usuario;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Gerenciamento de Categorias");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Adicionar", criarPainelAdicionar());
        tabPane.addTab("Editar", criarPainelEditar());
        tabPane.addTab("Excluir", criarPainelExcluir());
        add(tabPane, BorderLayout.CENTER);

        btnVoltar = new JButton("Voltar");
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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome da nova categoria:"), gbc);

        gbc.gridx = 1;
        txtNovaCategoria = new JTextField(20);
        txtNovaCategoria.setToolTipText("Digite o nome da categoria a ser adicionada");
        panel.add(txtNovaCategoria, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JButton btnAdd = new JButton("Adicionar");
        btnAdd.setPreferredSize(new Dimension(140, 25));
        btnAdd.addActionListener(e -> {
            String nome = txtNovaCategoria.getText().trim();
            String msg = usuario.getCategoria().adicionarCategoria(nome);
            JOptionPane.showMessageDialog(this, msg);
            atualizarCombos();
            txtNovaCategoria.setText("");
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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Selecione categoria:"), gbc);

        gbc.gridx = 1;
        cbEditarCategoria = new JComboBox<>(usuario.getCategoria().getCategorias().toArray(new String[0]));
        cbEditarCategoria.setPreferredSize(new Dimension(200, 25));
        panel.add(cbEditarCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Novo nome:"), gbc);

        gbc.gridx = 1;
        txtEditarCategoria = new JTextField(20);
        txtEditarCategoria.setToolTipText("Digite o novo nome para a categoria selecionada");
        panel.add(txtEditarCategoria, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        JButton btnEdit = new JButton("Salvar Alteração");
        btnEdit.setPreferredSize(new Dimension(140, 25));
        btnEdit.addActionListener(e -> {
            String atual = (String) cbEditarCategoria.getSelectedItem();
            String novoNome = txtEditarCategoria.getText().trim();
            String msg = usuario.getCategoria().editarCategoria(atual, novoNome);
            JOptionPane.showMessageDialog(this, msg);
            atualizarCombos();
            txtEditarCategoria.setText("");
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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Selecione categoria:"), gbc);

        gbc.gridx = 1;
        cbExcluirCategoria = new JComboBox<>(usuario.getCategoria().getCategorias().toArray(new String[0]));
        cbExcluirCategoria.setPreferredSize(new Dimension(200, 25));
        panel.add(cbExcluirCategoria, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JButton btnDel = new JButton("Excluir");
        btnDel.setPreferredSize(new Dimension(140, 25));
        btnDel.addActionListener(e -> {
            String alvo = (String) cbExcluirCategoria.getSelectedItem();
            int resp = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a categoria '" + alvo + "'?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                String msg = usuario.getCategoria().excluirCategoria(alvo);
                JOptionPane.showMessageDialog(this, msg);
                atualizarCombos();
            }
        });
        panel.add(btnDel, gbc);
        return panel;
    }

    private void atualizarCombos() {
        DefaultComboBoxModel<String> modelEdit = new DefaultComboBoxModel<>(usuario.getCategoria().getCategorias().toArray(new String[0]));
        DefaultComboBoxModel<String> modelDel = new DefaultComboBoxModel<>(usuario.getCategoria().getCategorias().toArray(new String[0]));
        cbEditarCategoria.setModel(modelEdit);
        cbExcluirCategoria.setModel(modelDel);
    }
}

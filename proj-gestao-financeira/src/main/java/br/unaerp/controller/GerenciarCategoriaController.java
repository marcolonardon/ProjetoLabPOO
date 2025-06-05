package br.unaerp.controller;

import br.unaerp.model.Categoria;
import br.unaerp.model.Transacao;
import br.unaerp.model.Usuario;
import br.unaerp.model.DAO.CategoriaDAO;
import br.unaerp.model.DAO.CategoriaDAOImpl;
import br.unaerp.model.DAO.TransacaoDAO;
import br.unaerp.model.DAO.TransacaoDAOImpl;
import br.unaerp.view.GerenciarCategoriaView;
import br.unaerp.view.MainView;

import javax.swing.*;
import java.util.List;

public class GerenciarCategoriaController {

    private final GerenciarCategoriaView view;
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();
    private final Usuario usuarioLogado;

    public GerenciarCategoriaController(Usuario usuario, GerenciarCategoriaView view) {
        this.usuarioLogado = usuario;
        this.view = view;
        initController();
        carregarCombos();
    }

    private void initController() {
        view.addAdicionarListener(e -> criarNovaCategoria());
        view.addSalvarAlteracaoListener(e -> editarCategoria());
        view.addExcluirListener(e -> excluirCategoria());
        view.addVoltarListener(e -> voltarParaMain());
    }

    private void carregarCombos() {
        List<Categoria> lista = categoriaDAO.buscarPorUsuario(usuarioLogado.getLogin());
        view.setListaCategoriasParaEditar(lista);
        view.setListaCategoriasParaExcluir(lista);
    }

    private void criarNovaCategoria() {
        String nome = view.getNomeNovaCategoria();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "O nome da categoria não pode ser vazio.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria existente = categoriaDAO.buscarPorNomeEUsuario(nome, usuarioLogado.getLogin());
        if (existente != null) {
            JOptionPane.showMessageDialog(view,
                    "Categoria já existe para este usuário.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria nova = new Categoria(nome, usuarioLogado);
        categoriaDAO.salvar(nova);
        JOptionPane.showMessageDialog(view,
                "Categoria adicionada com sucesso.",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        view.clearCampos();
        carregarCombos();
    }

    private void editarCategoria() {
        Categoria selecionada = view.getCategoriaSelecionadaParaEditar();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(view,
                    "Não há categoria selecionada para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String novoNome = view.getNovoNomeCategoria();
        if (novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "O novo nome não pode ser vazio.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria fromDb = categoriaDAO.buscarPorId(selecionada.getId());
        if (fromDb == null) {
            JOptionPane.showMessageDialog(view,
                    "Categoria não encontrada no banco.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            carregarCombos();
            return;
        }

        Categoria jaExiste = categoriaDAO.buscarPorNomeEUsuario(novoNome, usuarioLogado.getLogin());
        if (jaExiste != null && !jaExiste.getId().equals(fromDb.getId())) {
            JOptionPane.showMessageDialog(view,
                    "Já existe uma categoria com este novo nome.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        fromDb.setNome(novoNome);
        categoriaDAO.atualizar(fromDb);
        JOptionPane.showMessageDialog(view,
                "Categoria editada com sucesso.",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        view.clearCampos();
        carregarCombos();
    }

    private void excluirCategoria() {
        Categoria selecionada = view.getCategoriaSelecionadaParaExcluir();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(view,
                    "Não há categoria selecionada para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Transacao> transacoes = transacaoDAO.buscarPorUsuario(usuarioLogado.getLogin());
        boolean existeAssociada = transacoes.stream()
                .anyMatch(t -> t.getCategoria().getId().equals(selecionada.getId()));
        if (existeAssociada) {
            JOptionPane.showMessageDialog(view,
                    "Não é possível excluir a categoria \"" + selecionada.getNome() + "\" porque existem transações associadas.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(
                view,
                "Tem certeza que deseja excluir a categoria '" + selecionada.getNome() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );
        if (resp == JOptionPane.YES_OPTION) {
            categoriaDAO.deletar(selecionada);
            JOptionPane.showMessageDialog(view,
                    "Categoria excluída com sucesso.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarCombos();
        }
    }

    private void voltarParaMain() {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView(usuarioLogado);
            new MainController(usuarioLogado, mainView);
            mainView.setVisible(true);
        });
    }
}

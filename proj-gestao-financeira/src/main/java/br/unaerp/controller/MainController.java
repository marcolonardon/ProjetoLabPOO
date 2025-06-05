package br.unaerp.controller;

import br.unaerp.view.RegistrarTransacaoView;
import br.unaerp.view.GerenciarCategoriaView;
import br.unaerp.view.LoginView;
import br.unaerp.view.MainView;
import br.unaerp.view.VisualizarTransacaoView;
import br.unaerp.model.Usuario;

import javax.swing.*;

public class MainController {
    private final Usuario usuarioLogado;
    private final MainView mainView;

    public MainController(Usuario usuarioLogado, MainView mainView) {
        this.usuarioLogado = usuarioLogado;
        this.mainView = mainView;
        initController();
    }

    private void initController() {
        mainView.addCadastrarTransacaoListener(e -> abrirTelaRegistrarTransacao());

        mainView.addGerenciarCategoriasListener(e -> abrirTelaGerenciarCategorias());

        mainView.addVisualizarTransacoesListener(e -> abrirTelaVisualizarTransacoes());

        mainView.addLogoutListener(e -> {
            mainView.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                new LoginController(loginView);
                loginView.setVisible(true);
            });
        });
    }

    private void abrirTelaRegistrarTransacao() {
        mainView.dispose();
        SwingUtilities.invokeLater(() -> {
            RegistrarTransacaoView view = new RegistrarTransacaoView(usuarioLogado);
            new RegistrarTransacaoController(usuarioLogado, view);
            view.setVisible(true);
        });
    }

    private void abrirTelaGerenciarCategorias() {
        mainView.dispose();
        SwingUtilities.invokeLater(() -> {
            GerenciarCategoriaView view = new GerenciarCategoriaView(usuarioLogado);
            new GerenciarCategoriaController(usuarioLogado, view);
            view.setVisible(true);
        });
    }

    private void abrirTelaVisualizarTransacoes() {
        mainView.dispose();
        SwingUtilities.invokeLater(() -> {
            VisualizarTransacaoView view = new VisualizarTransacaoView(usuarioLogado);
            new VisualizarTransacaoController(usuarioLogado, view);
            view.setVisible(true);
        });
    }
}

package br.unaerp.controller;

import br.unaerp.model.Usuario;
import br.unaerp.model.DAO.UsuarioDAO;
import br.unaerp.model.DAO.UsuarioDAOImpl;
import br.unaerp.view.CadastroUsuarioView;
import br.unaerp.view.LoginView;
import br.unaerp.view.MainView;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController {
    private final LoginView loginView;
    private static Map<String, Usuario> usuariosEmMemoria = new HashMap<>();

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        carregarUsuariosDoBanco();
        initController();
    }

    private void initController() {
        loginView.addEntrarListener(e -> login());
        loginView.addNovoUsuarioListener(e -> abrirCadastroUsuario());
    }

    private void carregarUsuariosDoBanco() {
        UsuarioDAO dao = new UsuarioDAOImpl();
        List<Usuario> lista = dao.obterTodos();
        usuariosEmMemoria.clear();
        for (Usuario u : lista) {
            usuariosEmMemoria.put(u.getLogin(), u);
        }
    }

    private void login() {
        String usuario = loginView.getUsuario();
        String senha = loginView.getSenha();

        Usuario usuarioMemoria = usuariosEmMemoria.get(usuario);
        if (usuarioMemoria != null && usuarioMemoria.verificarSenha(senha)) {
            JOptionPane.showMessageDialog(loginView, "Login realizado com sucesso!");
            SwingUtilities.invokeLater(() -> {
                MainView mainView = new MainView(usuarioMemoria);
                new MainController(usuarioMemoria, mainView);
                mainView.setVisible(true);
                loginView.dispose();
            });
        } else {
            JOptionPane.showMessageDialog(loginView,
                    "Usuário ou senha incorretos.",
                    "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCadastroUsuario() {
        loginView.dispose();
        SwingUtilities.invokeLater(() -> {
            CadastroUsuarioView cadastroView = new CadastroUsuarioView();
            new CadastroUsuarioController(cadastroView);
            cadastroView.setVisible(true);
        });
    }

    public static void adicionarUsuarioNaMemoria(Usuario usuario) {
        usuariosEmMemoria.put(usuario.getLogin(), usuario);
    }
}

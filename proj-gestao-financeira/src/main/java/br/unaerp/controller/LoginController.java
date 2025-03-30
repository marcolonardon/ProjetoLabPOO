package br.unaerp.controller;

import br.unaerp.model.Usuario;
import br.unaerp.view.CadastroUsuarioView;
import br.unaerp.view.LoginView;
import br.unaerp.view.MainView;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private LoginView loginView;
    private static Map<String, Usuario> usuarios = new HashMap<>(); // Armazena os usuários cadastrados

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        initController();
    }

    private void initController() {
        loginView.addEntrarListener(e -> login());
        loginView.addNovoUsuarioListener(e -> abrirCadastroUsuario());
    }

    private void login() {
        String usuario = loginView.getUsuario();
        String senha = loginView.getSenha();
        Usuario u = usuarios.get(usuario);
        if (u != null && u.getSenha().equals(senha)) {
            JOptionPane.showMessageDialog(loginView, "Login realizado com sucesso!");
            System.out.println(u.getInformacoesUsuario());
//            SwingUtilities.invokeLater(() -> {
//                MainView mainView = new MainView();
//                new MainController(u, mainView);
//                mainView.setVisible(true);
//                loginView.dispose();
//            });
        } else {
            JOptionPane.showMessageDialog(loginView, "Usuário ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
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

    // Método para adicionar o usuário cadastrado no sistema
    public static void adicionarUsuario(Usuario usuario) {
        usuarios.put(usuario.getLogin(), usuario);
    }
}

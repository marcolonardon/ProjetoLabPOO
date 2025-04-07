package br.unaerp.controller;

import br.unaerp.model.Usuario;
import br.unaerp.model.UsuarioDAO;
import br.unaerp.view.CadastroUsuarioView;
import br.unaerp.view.LoginView;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CadastroUsuarioController {
    private CadastroUsuarioView cadastroView;

    public CadastroUsuarioController(CadastroUsuarioView cadastroView) {
        this.cadastroView = cadastroView;
        initController();
    }

    private void initController() {
        cadastroView.addSalvarListener(e -> salvarUsuario());
        cadastroView.addVoltarListener(e -> voltarParaLogin());
    }

    private void salvarUsuario() {
        String login = cadastroView.getLogin();
        String nome = cadastroView.getNome();
        String tipo = cadastroView.getTipoUsuario();
        String documento = cadastroView.getDocumento();
        String senha = cadastroView.getSenha();

        if (login.isEmpty() || nome.isEmpty() || documento.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(cadastroView, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(login, senha, nome, tipo, documento);

        new UsuarioDAO().salvarUsuario(novoUsuario);
        LoginController.adicionarUsuario(novoUsuario);

        JOptionPane.showMessageDialog(cadastroView, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        voltarParaLogin();
    }

    private void voltarParaLogin() {
        cadastroView.dispose();
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}

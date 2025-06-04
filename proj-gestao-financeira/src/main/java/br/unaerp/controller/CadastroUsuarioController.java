package br.unaerp.controller;

import br.unaerp.model.Categoria;
import br.unaerp.model.CategoriaDAO;
import br.unaerp.model.CategoriaDAOImpl;
import br.unaerp.model.Usuario;
import br.unaerp.model.UsuarioDAO;
import br.unaerp.model.UsuarioDAOImpl;
import br.unaerp.view.CadastroUsuarioView;
import br.unaerp.view.LoginView;

import javax.swing.*;

public class CadastroUsuarioController {
    private final CadastroUsuarioView cadastroView;
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

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
            JOptionPane.showMessageDialog(cadastroView,
                    "Preencha todos os campos!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuarioDAO.buscarPorLogin(login) != null) {
            JOptionPane.showMessageDialog(cadastroView,
                    "Já existe usuário com esse login.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(login, senha, nome, tipo, documento);

        Categoria catSalario = new Categoria("Salário", novoUsuario);
        Categoria catMercado = new Categoria("Mercado", novoUsuario);
        Categoria catSaude  = new Categoria("Saúde", novoUsuario);
        novoUsuario.adicionarCategoria(catSalario);
        novoUsuario.adicionarCategoria(catMercado);
        novoUsuario.adicionarCategoria(catSaude);
        LoginController.adicionarUsuarioNaMemoria(novoUsuario);
        usuarioDAO.salvar(novoUsuario);
        JOptionPane.showMessageDialog(cadastroView,
                "Usuário cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
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

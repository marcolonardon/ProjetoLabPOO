package br.unaerp.model;

import br.unaerp.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Autenticacao {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioAutenticado = null;

    public void cadastrarUsuario(String login, String senha, String nome, String tipo, String documento) {
        if (usuarios.containsKey(login)) {
            System.out.println("Erro: Usuário já existe!");
        } else {
            usuarios.put(login, new Usuario(login, senha, nome, tipo, documento));
            System.out.println("Usuário cadastrado com sucesso!");
        }
    }

    public boolean fazerLogin(String login, String senha) {
        Usuario usuario = usuarios.get(login);
        if (usuario != null && usuario.verificarSenha(senha)) {
            usuarioAutenticado = usuario;
            System.out.println("Login bem-sucedido! Bem-vindo, " + login);
            return true;
        } else {
            System.out.println("Erro: Login ou senha incorretos.");
            return false;
        }
    }

    public void fazerLogout() {
        if (usuarioAutenticado != null) {
            System.out.println("Usuário " + usuarioAutenticado.getLogin() + " fez logout.");
            usuarioAutenticado = null;
        } else {
            System.out.println("Nenhum usuário está autenticado.");
        }
    }

    public boolean estaAutenticado() {
        return usuarioAutenticado != null;
    }
}
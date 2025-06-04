package br.unaerp.model;
import java.util.HashMap;
import java.util.Map;

public class Autenticacao {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioAutenticado = null;


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
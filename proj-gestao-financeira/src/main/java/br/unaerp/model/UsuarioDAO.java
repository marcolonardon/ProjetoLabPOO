// src/main/java/br/unaerp/model/UsuarioDAO.java
package br.unaerp.model;

import java.util.List;

public interface UsuarioDAO {
    void salvar(Usuario usuario);
    Usuario buscarPorLogin(String login);
    List<Usuario> obterTodos();
    void atualizar(Usuario usuario);
    void deletar(Usuario usuario);
}

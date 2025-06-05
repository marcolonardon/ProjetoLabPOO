package br.unaerp.model.DAO;

import br.unaerp.model.Usuario;

import java.util.List;

public interface UsuarioDAO {
    void salvar(Usuario usuario);

    Usuario buscarPorLogin(String login);

    List<Usuario> obterTodos();

    void atualizar(Usuario usuario);

    void deletar(Usuario usuario);
}

package br.unaerp.model.DAO;

import br.unaerp.model.Categoria;

import java.util.List;

public interface CategoriaDAO {
    void salvar(Categoria categoria);
    Categoria buscarPorId(Integer id);
    List<Categoria> buscarPorUsuario(String loginUsuario);
    Categoria buscarPorNomeEUsuario(String nome, String loginUsuario);
    void atualizar(Categoria categoria);
    void deletar(Categoria categoria);
}

package br.unaerp.model;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private List<String> categorias;

    public Categoria() {
        categorias = new ArrayList<>();
        categorias.add("Salário");
        categorias.add("Mercado");
        categorias.add("Saúde");
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public String adicionarCategoria(String novaCategoria) {
        if (!categorias.contains(novaCategoria)) {
            categorias.add(novaCategoria);
            return "Categoria adicionada com sucesso.";
        } else {
            return "Categoria já existe.";
        }
    }

    public String editarCategoria(String categoriaAtual, String novaCategoria) {
        if (categorias.contains(categoriaAtual)) {
            int index = categorias.indexOf(categoriaAtual);
            categorias.set(index, novaCategoria);
            return "Categoria editada com sucesso.";
        } else {
            return "Categoria não encontrada.";
        }
    }

    public String excluirCategoria(String categoria) {
        if (categorias.contains(categoria)) {
            categorias.remove(categoria);
            return "Categoria excluída com sucesso.";
        } else {
            return "Categoria não encontrada.";
        }
    }
}

package br.unaerp.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Caminho onde o arquivo será salvo
    private static final String ARQUIVO_USUARIOS = "usuarios.txt";

    // Método para salvar um usuário no arquivo
    public void salvarUsuario(Usuario usuario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_USUARIOS, true))) {
            // Escreve os dados do usuário no formato desejado
            writer.write(usuario.getLogin() + ";" + usuario.getSenha() + ";" + usuario.getNome() + ";" + usuario.getTipo() + ";" + usuario.getDocumento());
            writer.newLine();  // Adiciona uma nova linha após o registro
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar os usuários registrados do arquivo
    public List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        File arquivo = new File(ARQUIVO_USUARIOS);

        // Verifica se o arquivo existe, caso contrário, retorna uma lista vazia
        if (!arquivo.exists()) {
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Separa os dados do usuário (assumindo que o formato é "nome;senha;...")
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    // Cria um novo objeto Usuario e adiciona à lista
                    Usuario usuario = new Usuario(dados[0], dados[1], dados[2], dados[3], dados[4]);
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // Método para verificar se o arquivo existe, caso contrário, cria
    private void verificarArquivoExistente() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public UsuarioDAO() {
        verificarArquivoExistente();
    }
}

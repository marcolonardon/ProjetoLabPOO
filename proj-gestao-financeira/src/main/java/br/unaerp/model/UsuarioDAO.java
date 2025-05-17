package br.unaerp.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String ARQUIVO_USUARIOS = "usuarios.txt";

    public void salvarUsuario(Usuario usuario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_USUARIOS, true))) {
            writer.write(usuario.getLogin() + ";" + usuario.getSenha() + ";" + usuario.getNome() + ";" + usuario.getTipo() + ";" + usuario.getDocumento());
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        File arquivo = new File(ARQUIVO_USUARIOS);

        if (!arquivo.exists()) {
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    Usuario usuario = new Usuario(dados[0], dados[1], dados[2], dados[3], dados[4]);
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

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

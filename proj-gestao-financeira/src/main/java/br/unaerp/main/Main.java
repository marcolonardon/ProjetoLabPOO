package br.unaerp.main;

import br.unaerp.model.Usuario;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Usuario usuario = new Usuario("João Silva", "Pessoa Física");
        usuario.setDocumento("123.456.789-00");

        usuario.registrarTransacao(500, "Receita", "Salário", "Salário", 10, 3, 2025);
        usuario.registrarTransacao(200, "Despesa","Mercado", "Conta de luz", 11, 3, 2025);
        usuario.registrarTransacao(300, "Despesa","Mercado", "Compra de supermercado", 12, 3, 2025);
        usuario.registrarTransacao(150, "Receita", "Salário","Bônus", 13, 3, 2025);
        usuario.registrarTransacao(100, "Despesa", "Mercado","Lanche", 14, 3, 2025);

        usuario.editarClassificacao("Mercado", "MercadoEditado");
        usuario.adicionarClassificacao("NovaClassificacao");
        usuario.adicionarClassificacao("ClassificacaoParaDeletar");
        usuario.excluirClassificacao("ClassificacaoParaDeletar");


        usuario.imprimirInformacoesUsuario();

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nDeseja filtrar transações? (S/N)");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            usuario.filtrarTransacoes();
        }
        scanner.close();
    }
}
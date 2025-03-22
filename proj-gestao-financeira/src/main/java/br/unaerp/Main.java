package br.unaerp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Usuario usuario = new Usuario("João Silva", "Pessoa Física");
        usuario.setDocumento("123.456.789-00");

        Conta conta = new Conta(1, usuario);

        conta.depositar(1000);
        conta.sacar(300);

        System.out.print("Informe o valor da transação de João: ");
        float valorJoao = scanner.nextFloat();
        scanner.nextLine();

        System.out.print("Informe a categoria da transação de João: ");
        String categoriaJoao = scanner.nextLine();

        System.out.print("Informe a descrição da transação de João: ");
        String descricaoJoao = scanner.nextLine();

        usuario.registrarTransacao(valorJoao, categoriaJoao, descricaoJoao);

        usuario.imprimirInformacoesUsuario();

        scanner.close();
    }
}

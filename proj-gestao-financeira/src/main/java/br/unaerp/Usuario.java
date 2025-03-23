package br.unaerp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Usuario {
    private String nome;
    private String tipo;
    private String documento;
    private List<Transacao> transacoes;

    public Usuario(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.transacoes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void registrarTransacao(float valor, String categoria, String descricao, int dia, int mes, int ano) {
        transacoes.add(new Transacao(valor, categoria, descricao, dia, mes, ano));
    }

    public float calcularSaldoTotal() {
        float saldo = 0;
        for (Transacao t : transacoes) {
            if (t.getCategoria().equalsIgnoreCase("Receita")) {
                saldo += t.getValor();
            } else if (t.getCategoria().equalsIgnoreCase("Despesa")) {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }

    public void imprimirInformacoesUsuario() {
        System.out.println("Informações do usuário: " + nome);
        System.out.println("Tipo: " + tipo);
        if (tipo.equalsIgnoreCase("Pessoa Física")) {
            System.out.println("CPF: " + documento);
        } else {
            System.out.println("CNPJ: " + documento);
        }
        System.out.println("\nTransações:");
        for (Transacao t : transacoes) {
            System.out.println("Data: " + t.getDia() + "/" + t.getMes() + "/" + t.getAno() +
                    " | Categoria: " + t.getCategoria() +
                    " | Valor: R$ " + t.getValor() +
                    " | Descrição: " + t.getDescricao());
        }
        System.out.println("\nSaldo Total: R$ " + calcularSaldoTotal());
    }

    public void filtrarTransacoes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha o filtro: 1 - Data | 2 - Classificação | 3 - Categoria");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        List<Transacao> resultado = new ArrayList<>();
        switch (escolha) {
            case 1:
                System.out.println("Digite a data (dd mm aaaa):");
                int dia = scanner.nextInt();
                int mes = scanner.nextInt();
                int ano = scanner.nextInt();
                for (Transacao t : transacoes) {
                    if (t.getDia() == dia && t.getMes() == mes && t.getAno() == ano) {
                        resultado.add(t);
                    }
                }
                break;
            case 2:
                System.out.println("Digite a categora (Despesa ou Receita):");
                String classificacao = scanner.nextLine();
                for (Transacao t : transacoes) {
                    if (t.getCategoria().equalsIgnoreCase(classificacao)) {
                        resultado.add(t);
                    }
                }
                break;
            case 3:
                // classificacao = categoria???
                System.out.println("Digite a categoria (Despesa ou Receita):");
                String categoria = scanner.nextLine();
                for (Transacao t : transacoes) {
                    if (t.getCategoria().equalsIgnoreCase(categoria)) {
                        resultado.add(t);
                    }
                }
                break;
            default:
                System.out.println("Opção inválida.");
                return;
        }
        if (resultado.isEmpty()) {
            System.out.println("Nenhuma transação encontrada com esse filtro.");
        } else {
            System.out.println("Transações filtradas:");
            for (Transacao t : resultado) {
                t.imprimirTransacao();
            }
        }
    }
}

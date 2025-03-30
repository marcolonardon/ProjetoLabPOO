package br.unaerp.model;

import java.util.ArrayList;
import java.util.List;

public class Conta {
    private int id;
    private float saldo;
    private List<Transacao> transacoes;

    public Conta(int id) {
        this.id = id;
        this.saldo = 0;
        this.transacoes = new ArrayList<>();
    }

    public float getSaldo() {
        return saldo;
    }

    public void depositar(float valor, int dia, int mes, int ano) {
        if (valor <= 0) {
            System.out.println("O valor do depósito deve ser positivo.");
            return;
        }
        saldo += valor;
        registrarTransacao(valor, "Receita", "Depósito", "Depósito na conta", dia, mes, ano);
    }

    public void sacar(float valor, int dia, int mes, int ano) {
        if (valor <= 0) {
            System.out.println("O valor do saque deve ser positivo.");
            return;
        }
        if (saldo < valor) {
            System.out.println("Saldo insuficiente.");
            return;
        }
        saldo -= valor;
        registrarTransacao(valor, "Despesa", "Saque", "Saque da conta", dia, mes, ano);
    }

    public void registrarTransacao(float valor, String categoria, String classificacao, String descricao, int dia, int mes, int ano) {
        transacoes.add(new Transacao(valor, categoria, classificacao, descricao, dia, mes, ano));
    }

    public void imprimirExtrato() {
        System.out.println("Extrato da Conta ID: " + id);
        System.out.println("Saldo Atual: R$ " + saldo);
        System.out.println("Transações:");
        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
        } else {
            for (Transacao t : transacoes) {
                System.out.println("Data: " + t.getDia() + "/" + t.getMes() + "/" + t.getAno() +
                        " | Categoria: " + t.getCategoria() +
                        " | Classificação: " + t.getClassificacao() +
                        " | Valor: R$ " + t.getValor() +
                        " | Descrição: " + t.getDescricao());
            }
        }
    }
}
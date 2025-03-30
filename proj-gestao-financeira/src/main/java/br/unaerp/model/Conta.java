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
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        saldo += valor;
        registrarTransacao(valor, "Receita", "Depósito", "Depósito na conta", dia, mes, ano);
    }

    public void sacar(float valor, int dia, int mes, int ano) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        if (saldo < valor) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        saldo -= valor;
        registrarTransacao(valor, "Despesa", "Saque", "Saque da conta", dia, mes, ano);
    }

    public void registrarTransacao(float valor, String categoria, String classificacao, String descricao, int dia, int mes, int ano) {
        transacoes.add(new Transacao(valor, categoria, classificacao, descricao, dia, mes, ano));
    }

    public String getExtrato() {
        StringBuilder extrato = new StringBuilder();
        extrato.append("Extrato da Conta ID: ").append(id).append("\n");
        extrato.append("Saldo Atual: R$ ").append(saldo).append("\n");
        extrato.append("Transações:\n");
        if (transacoes.isEmpty()) {
            extrato.append("Nenhuma transação registrada.\n");
        } else {
            for (Transacao t : transacoes) {
                extrato.append("Data: ").append(t.getDia()).append("/")
                        .append(t.getMes()).append("/")
                        .append(t.getAno()).append(" | Categoria: ").append(t.getCategoria())
                        .append(" | Classificação: ").append(t.getClassificacao())
                        .append(" | Valor: R$ ").append(t.getValor())
                        .append(" | Descrição: ").append(t.getDescricao()).append("\n");
            }
        }
        return extrato.toString();
    }
}

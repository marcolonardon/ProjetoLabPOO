package br.unaerp;

import java.util.ArrayList;
import java.util.List;

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
}

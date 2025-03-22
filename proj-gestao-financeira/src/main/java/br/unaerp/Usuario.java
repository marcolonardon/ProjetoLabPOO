package br.unaerp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {
    private String nome;
    private String tipo;
    private String cpf;
    private String cnpj;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (this.tipo.equals("Pessoa Física")) {
            this.cpf = cpf;
        } else {
            System.out.println("CNPJ não pode ser definido para Pessoa Física.");
        }
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if (this.tipo.equals("Pessoa Jurídica")) {
            this.cnpj = cnpj;
        } else {
            System.out.println("CPF não pode ser definido para Pessoa Jurídica.");
        }
    }

    public void setDocumento(String documento) {
        if (this.tipo.equals("Pessoa Física")) {
            setCpf(documento);
        } else if (this.tipo.equals("Pessoa Jurídica")) {
            setCnpj(documento);
        } else {
            System.out.println("Tipo de usuário inválido.");
        }
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void registrarTransacao(float valor, String categoria, String descricao) {
        Transacao transacao = new Transacao(valor, categoria, descricao);
        transacoes.add(transacao);
    }

    public float calcularSaldoTotal() {
        float saldoTotal = 0;

        for (Transacao transacao : transacoes) {
            if (transacao.getCategoria().equals("Receita")) {
                saldoTotal += transacao.getValor(); // Soma as receitas
            } else if (transacao.getCategoria().equals("Despesa")) {
                saldoTotal -= transacao.getValor(); // Subtrai as despesas
            }
        }

        return saldoTotal;
    }

    public void imprimirInformacoesUsuario() {
        System.out.println("Informações de " + nome + ":");
        System.out.println("Tipo: " + tipo);
        if (tipo.equals("Pessoa Física")) {
            System.out.println("CPF: " + cpf);
        } else {
            System.out.println("CNPJ: " + cnpj);
        }

        System.out.println("\nTransações Registradas:");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Transacao transacao : transacoes) {
            System.out.println("\nValor: R$ " + transacao.getValor());
            System.out.println("Categoria: " + transacao.getCategoria());
            System.out.println("Data: " + sdf.format(transacao.getData())); // Agora formatando corretamente
            System.out.println("Descrição: " + transacao.getDescricao());
        }

        float saldoTotal = calcularSaldoTotal();
        System.out.println("\nSaldo Total: R$ " + saldoTotal);
    }


    @Override
    public String toString() {
        if (tipo.equals("Pessoa Física")) {
            return "Nome: " + nome + ", Tipo: " + tipo + ", CPF: " + cpf;
        } else {
            return "Nome: " + nome + ", Tipo: " + tipo + ", CNPJ: " + cnpj;
        }
    }
}

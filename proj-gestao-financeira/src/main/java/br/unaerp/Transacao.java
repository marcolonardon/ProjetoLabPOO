package br.unaerp;

public class Transacao {
    private float valor;
    private String categoria;
    private String descricao;
    private int dia;
    private int mes;
    private int ano;

    public Transacao(float valor, String categoria, String descricao, int dia, int mes, int ano) {
        this.valor = valor;
        this.categoria = categoria;
        this.descricao = descricao;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public float getValor() {
        return valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }
}

package br.unaerp.model;

public class Transacao {
    private float valor;
    private String categoria;
    private String classificacao;
    private String descricao;
    private int dia;
    private int mes;
    private int ano;

    public Transacao(float valor, String categoria, String classificacao, String descricao, int dia, int mes, int ano) {
        this.valor = valor;
        this.categoria = categoria;
        this.classificacao = classificacao;
        this.descricao = descricao;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public String getTransacaoString() {
        return String.format("%02d/%02d/%04d - %s: R$ %.2f (%s) (%s)",
                dia, mes, ano, descricao, valor, classificacao, categoria);
    }

    public float getValor() {
        return valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia.");
        }
        this.categoria = categoria;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
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

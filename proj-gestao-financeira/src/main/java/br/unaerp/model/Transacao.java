package br.unaerp.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "valor", nullable = false)
    private float valor;

    @Column(name = "classificacao", nullable = false, length = 20)
    private String classificacao; // "Receita" ou "Despesa"

    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_login", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Transacao() {
        // Construtor para JPA
    }

    public Transacao(float valor,
                     String classificacao,
                     String descricao,
                     LocalDate data,
                     Usuario usuario,
                     Categoria categoria) {
        this.valor = valor;
        this.classificacao = classificacao;
        this.descricao = descricao;
        this.data = data;
        this.usuario = usuario;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
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

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

}

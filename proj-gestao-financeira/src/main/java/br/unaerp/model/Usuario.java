package br.unaerp.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Usuario {
    private final String login;
    private String senha;
    private String nome;
    private String tipo;
    private String documento;
    private List<Transacao> transacoes;
    private Categoria categoria;

    public Usuario(String login, String senha, String nome, String tipo, String documento) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.tipo = tipo;
        this.documento = documento;
        this.transacoes = new ArrayList<>();
        this.categoria = new Categoria();
    }

    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean verificarSenha(String senha) { return this.senha.equals(senha); }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public Categoria getCategoria() { return categoria; }
    public List<String> getCategorias() { return categoria.getCategorias(); }
    public List<Transacao> getTransacoes() { return transacoes; }
    public void setTransacoes(List<Transacao> transacoes) { this.transacoes = transacoes; }

    public void registrarTransacao(float valor,
                                   String categoriaNome,
                                   String classificacao,
                                   String descricao,
                                   int dia, int mes, int ano) {
        transacoes.add(new Transacao(valor, categoriaNome, classificacao, descricao, dia, mes, ano));
    }

    public float calcularSaldoTotal() {
        float saldo = 0;
        for (Transacao t : transacoes) {
            if (t.getClassificacao().equalsIgnoreCase("Receita")) {
                saldo += t.getValor();
            } else if (t.getClassificacao().equalsIgnoreCase("Despesa")) {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }

    public String getInformacoesUsuario() {
        StringBuilder sb = new StringBuilder();
        sb.append("Informações do usuário: ").append(nome).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        if (tipo.equalsIgnoreCase("Pessoa Física"))
            sb.append("CPF: ").append(documento).append("\n");
        else
            sb.append("CNPJ: ").append(documento).append("\n");

        sb.append("\nTransações:\n");
        FormatarData(transacoes, sb);

        sb.append("\nSaldo Total: R$ ").append(String.format("%.2f", calcularSaldoTotal())).append("\n");
        return sb.toString();
    }

    public String filtrarTransacoes(int filtro,
                                    int diaInicio, int mesInicio, int anoInicio,
                                    int diaFim, int mesFim, int anoFim,
                                    String filtroExtra) {
        float periodoReceitas = 0, periodoDespesas = 0;
        List<Transacao> resultado = new ArrayList<>();
        float totalReceitasFiltro = 0, totalDespesasFiltro = 0;

        for (Transacao t : transacoes) {
            if (isDataNoIntervalo(t, diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim)) {
                if (t.getClassificacao().equalsIgnoreCase("Receita"))
                    periodoReceitas += t.getValor();
                else if (t.getClassificacao().equalsIgnoreCase("Despesa"))
                    periodoDespesas += t.getValor();
            }
        }

        for (Transacao t : transacoes) {
            boolean inRange = isDataNoIntervalo(t, diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim);
            switch (filtro) {
                case 1:
                    if (inRange) {
                        resultado.add(t);
                    }
                    break;
                case 2:
                    if (inRange && t.getCategoria().equalsIgnoreCase(filtroExtra)) {
                        resultado.add(t);
                    }
                    break;
                case 3:
                    if (inRange && t.getClassificacao().equalsIgnoreCase(filtroExtra)) {
                        resultado.add(t);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Opção de filtro inválida.");
            }
        }

        for (Transacao t : resultado) {
            if (t.getClassificacao().equalsIgnoreCase("Receita"))
                totalReceitasFiltro += t.getValor();
            else if (t.getClassificacao().equalsIgnoreCase("Despesa"))
                totalDespesasFiltro += t.getValor();
        }

        StringBuilder sb = new StringBuilder();
        if (resultado.isEmpty()) {
            sb.append("Nenhuma transação encontrada com esse filtro.\n");
        } else {
            sb.append("Transações filtradas:\n");
            FormatarData(resultado, sb);
            sb.append("\nResumo do filtro:\n");
            sb.append("Total de Receitas (Filtro): R$ ").append(String.format("%.2f", totalReceitasFiltro)).append("\n");
            sb.append("Total de Despesas (Filtro): R$ ").append(String.format("%.2f", totalDespesasFiltro)).append("\n");
            sb.append("Saldo (Filtro): R$ ").append(String.format("%.2f", totalReceitasFiltro - totalDespesasFiltro)).append("\n");
        }

        sb.append("\nResumo do período:\n");
        sb.append("Total de Receitas: R$ ").append(String.format("%.2f", periodoReceitas)).append("\n");
        sb.append("Total de Despesas: R$ ").append(String.format("%.2f", periodoDespesas)).append("\n");
        sb.append("Saldo no período: R$ ").append(String.format("%.2f", periodoReceitas - periodoDespesas)).append("\n");
        return sb.toString();
    }

    private void FormatarData(List<Transacao> resultado, StringBuilder sb) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Transacao t : resultado) {
            sb.append(fmt.format(LocalDate.of(t.getAno(), t.getMes(), t.getDia())))
                    .append(" | ")
                    .append(t.getClassificacao())
                    .append(": R$ ")
                    .append(String.format("%.2f", t.getValor()))
                    .append(" (Categoria: ")
                    .append(t.getCategoria())
                    .append(")\n");
        }
    }

    private boolean isDataNoIntervalo(Transacao t,
                                      int diaInicio, int mesInicio, int anoInicio,
                                      int diaFim, int mesFim, int anoFim) {
        int dataTransacao = t.getAno() * 10000 + t.getMes() * 100 + t.getDia();
        int dataInicio = anoInicio * 10000 + mesInicio * 100 + diaInicio;
        int dataFim = anoFim * 10000 + mesFim * 100 + diaFim;
        return dataTransacao >= dataInicio && dataTransacao <= dataFim;
    }
}

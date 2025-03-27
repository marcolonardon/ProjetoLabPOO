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
            if (t.getCategoria().equalsIgnoreCase("Receita"))
                saldo += t.getValor();
            else if (t.getCategoria().equalsIgnoreCase("Despesa"))
                saldo -= t.getValor();
        }
        return saldo;
    }

    public void imprimirInformacoesUsuario() {
        System.out.println("Informações do usuário: " + nome);
        System.out.println("Tipo: " + tipo);
        if (tipo.equalsIgnoreCase("Pessoa Física"))
            System.out.println("CPF: " + documento);
        else
            System.out.println("CNPJ: " + documento);
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
        int escolha = -1;
        while (true) {
            System.out.println("Escolha o filtro: 1 - Data | 2 - Classificação | 3 - Categoria");
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                scanner.nextLine();
            }
        }
        int[] periodo = obterPeriodoFiltragem(scanner);
        int diaInicio = periodo[0], mesInicio = periodo[1], anoInicio = periodo[2];
        int diaFim = periodo[3], mesFim = periodo[4], anoFim = periodo[5];

        float periodoReceitas = 0, periodoDespesas = 0;
        for (Transacao t : transacoes) {
            if (isDataNoIntervalo(t, diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim)) {
                if (t.getCategoria().equalsIgnoreCase("Receita"))
                    periodoReceitas += t.getValor();
                else if (t.getCategoria().equalsIgnoreCase("Despesa"))
                    periodoDespesas += t.getValor();
            }
        }

        List<Transacao> resultado = new ArrayList<>();
        float totalReceitasFiltro = 0, totalDespesasFiltro = 0;
        switch (escolha) {
            case 1:
                for (Transacao t : transacoes) {
                    if (isDataNoIntervalo(t, diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim)) {
                        resultado.add(t);
                        if (t.getCategoria().equalsIgnoreCase("Receita"))
                            totalReceitasFiltro += t.getValor();
                        else if (t.getCategoria().equalsIgnoreCase("Despesa"))
                            totalDespesasFiltro += t.getValor();
                    }
                }
                break;
            case 2:
                System.out.println("Digite a classificação (Receita ou Despesa):");
                String classificacao = scanner.nextLine();
                for (Transacao t : transacoes) {
                    if (isDataNoIntervalo(t, diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim)
                            && t.getCategoria().equalsIgnoreCase(classificacao)) {
                        resultado.add(t);
                        if (t.getCategoria().equalsIgnoreCase("Receita"))
                            totalReceitasFiltro += t.getValor();
                        else if (t.getCategoria().equalsIgnoreCase("Despesa"))
                            totalDespesasFiltro += t.getValor();
                    }
                }
                break;
            case 3:
                System.out.println("Digite a categoria (Receita ou Despesa):");
                String categoriaInput = scanner.nextLine();
                for (Transacao t : transacoes) {
                    if (isDataNoIntervalo(t, diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim)
                            && t.getCategoria().equalsIgnoreCase(categoriaInput)) {
                        resultado.add(t);
                        if (t.getCategoria().equalsIgnoreCase("Receita"))
                            totalReceitasFiltro += t.getValor();
                        else if (t.getCategoria().equalsIgnoreCase("Despesa"))
                            totalDespesasFiltro += t.getValor();
                    }
                }
                break;
            default:
                System.out.println("Opção inválida.");
                return;
        }
        if (resultado.isEmpty())
            System.out.println("Nenhuma transação encontrada com esse filtro.");
        else {
            System.out.println("Transações filtradas:");
            for (Transacao t : resultado)
                t.imprimirTransacao();
            float saldoFiltro = totalReceitasFiltro - totalDespesasFiltro;
            System.out.println("\nResumo do filtro:");
            System.out.println("Total de Receitas (Filtro): R$ " + totalReceitasFiltro);
            System.out.println("Total de Despesas (Filtro): R$ " + totalDespesasFiltro);
            System.out.println("Saldo (Filtro): R$ " + saldoFiltro);
        }
        System.out.println("\nResumo do período:");
        System.out.println("Total de Receitas: R$ " + periodoReceitas);
        System.out.println("Total de Despesas: R$ " + periodoDespesas);
        System.out.println("Saldo no período: R$ " + (periodoReceitas - periodoDespesas));
    }

    private int[] obterPeriodoFiltragem(Scanner scanner) {
        System.out.println("Digite a data inicial (dd mm aaaa):");
        int diaInicio = scanner.nextInt();
        int mesInicio = scanner.nextInt();
        int anoInicio = scanner.nextInt();
        System.out.println("Digite a data final (dd mm aaaa):");
        int diaFim = scanner.nextInt();
        int mesFim = scanner.nextInt();
        int anoFim = scanner.nextInt();
        scanner.nextLine();
        return new int[]{diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim};
    }

    private boolean isDataNoIntervalo(Transacao t, int diaInicio, int mesInicio, int anoInicio, int diaFim, int mesFim, int anoFim) {
        int dataTransacao = t.getAno() * 10000 + t.getMes() * 100 + t.getDia();
        int dataInicio = anoInicio * 10000 + mesInicio * 100 + diaInicio;
        int dataFim = anoFim * 10000 + mesFim * 100 + diaFim;
        return dataTransacao >= dataInicio && dataTransacao <= dataFim;
    }
}

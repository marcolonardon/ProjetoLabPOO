package br.unaerp.model;

import br.unaerp.model.DAO.TransacaoDAO;
import br.unaerp.model.DAO.TransacaoDAOImpl;

import java.util.List;

public class Conta {

    private final Usuario usuario;
    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();

    public Conta(Usuario usuario) {
        this.usuario = usuario;
    }

    public float getSaldo() {
        List<Transacao> todasTransacoes = transacaoDAO.buscarPorUsuario(usuario.getLogin());
        float saldo = 0f;
        for (Transacao t : todasTransacoes) {
            if ("Receita".equalsIgnoreCase(t.getClassificacao())) {
                saldo += t.getValor();
            } else if ("Despesa".equalsIgnoreCase(t.getClassificacao())) {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }

}

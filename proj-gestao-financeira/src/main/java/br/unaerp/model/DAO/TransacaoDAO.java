package br.unaerp.model.DAO;

import br.unaerp.model.Transacao;

import java.util.List;

public interface TransacaoDAO {
    void salvar(Transacao transacao);
    Transacao buscarPorId(Integer id);
    List<Transacao> buscarPorUsuario(String loginUsuario);
    List<Transacao> buscarPorUsuarioEPeriodo(String loginUsuario,
                                             java.time.LocalDate dataInicio,
                                             java.time.LocalDate dataFim);
    void atualizar(Transacao transacao);
    void deletar(Transacao transacao);
}

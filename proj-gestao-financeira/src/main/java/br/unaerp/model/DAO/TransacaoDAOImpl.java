package br.unaerp.model.DAO;

import br.unaerp.model.Transacao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;

public class TransacaoDAOImpl implements TransacaoDAO {
    private static final SessionFactory factory = new Configuration().configure().buildSessionFactory();

    @Override
    public void salvar(Transacao transacao) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.persist(transacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Transacao buscarPorId(Integer id) {
        try (Session session = factory.openSession()) {
            return session.get(Transacao.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transacao> buscarPorUsuario(String loginUsuario) {
        try (Session session = factory.openSession()) {
            String hql =
                    "SELECT t " +
                            "FROM Transacao t " +
                            "JOIN FETCH t.categoria c " +
                            "WHERE t.usuario.login = :loginUsuario " +
                            "ORDER BY t.data DESC";

            return session.createQuery(hql)
                    .setParameter("loginUsuario", loginUsuario)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Transacao> buscarPorUsuarioEPeriodo(String loginUsuario, LocalDate dataInicio, LocalDate dataFim) {
        try (Session session = factory.openSession()) {
            String hql = "FROM Transacao t WHERE t.usuario.login = :loginUsuario "
                    + "AND t.data BETWEEN :dataInicio AND :dataFim "
                    + "ORDER BY t.data DESC";
            return session.createQuery(hql)
                    .setParameter("loginUsuario", loginUsuario)
                    .setParameter("dataInicio", dataInicio)
                    .setParameter("dataFim", dataFim)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void atualizar(Transacao transacao) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.merge(transacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Transacao transacao) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.remove(transacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}

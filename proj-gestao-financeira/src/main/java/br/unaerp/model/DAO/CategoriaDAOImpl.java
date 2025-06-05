package br.unaerp.model.DAO;

import br.unaerp.model.Categoria;
import br.unaerp.model.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {
    private static final SessionFactory factory = new Configuration().configure().buildSessionFactory();

    @Override
    public void salvar(Categoria categoria) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            String loginUsuario = categoria.getUsuario().getLogin();
            Usuario managedUser = session.get(Usuario.class, loginUsuario);
            categoria.setUsuario(managedUser);
            session.persist(categoria);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Categoria buscarPorId(Integer id) {
        try (Session session = factory.openSession()) {
            return session.get(Categoria.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Categoria> buscarPorUsuario(String loginUsuario) {
        try (Session session = factory.openSession()) {
            return session.createQuery(
                            "FROM Categoria c WHERE c.usuario.login = :loginUsuario"
                    )
                    .setParameter("loginUsuario", loginUsuario)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Categoria buscarPorNomeEUsuario(String nome, String loginUsuario) {
        try (Session session = factory.openSession()) {
            return session.createQuery(
                            "FROM Categoria c WHERE c.nome = :nome AND c.usuario.login = :loginUsuario",
                            Categoria.class
                    )
                    .setParameter("nome", nome)
                    .setParameter("loginUsuario", loginUsuario)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void atualizar(Categoria categoria) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.merge(categoria);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Categoria categoria) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.remove(categoria);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}

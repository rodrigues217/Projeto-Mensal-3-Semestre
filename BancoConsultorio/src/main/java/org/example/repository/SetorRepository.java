package org.example.repository;

import jakarta.persistence.*;
import org.example.entities.Setor;
import org.example.entities.Funcionario;

import java.util.List;

public class SetorRepository {

    private EntityManager em;

    public SetorRepository(EntityManager em) {
        this.em = em;
    }

    public void salvar(Setor setor) {
        try {
            em.getTransaction().begin();
            em.persist(setor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Setor> buscarTodos() {
        return em.createQuery("SELECT s FROM Setor s", Setor.class).getResultList();
    }

    public Setor buscarPorId(long id) {
        return em.find(Setor.class, id);
    }

    public void atualizar(Setor setor) {
        try {
            em.getTransaction().begin();
            em.merge(setor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void remover(Setor setor) {
        try {
            em.getTransaction().begin();
            setor = em.find(Setor.class, setor.getId());
            if (setor != null) {
                em.remove(setor);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para listar setores com os funcionários associados
    public List<Setor> listarSetoresComFuncionarios() {
        return em.createQuery("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios", Setor.class).getResultList();
    }
}

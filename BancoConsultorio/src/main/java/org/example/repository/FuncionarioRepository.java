package org.example.repository;

import jakarta.persistence.*;
import org.example.entities.Funcionario;
import org.example.entities.Setor;

import java.util.List;

public class FuncionarioRepository {

    private EntityManager em;

    public FuncionarioRepository(EntityManager em) {
        this.em = em;
    }

    public void salvar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();
            em.persist(funcionario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Funcionario> buscarTodos() {
        return em.createQuery("SELECT f FROM Funcionario f", Funcionario.class).getResultList();
    }

    public Funcionario buscarPorId(long id) {
        return em.find(Funcionario.class, id);
    }

    public void atualizar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();
            em.merge(funcionario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void remover(Funcionario funcionario) {
        try {
            em.getTransaction().begin();
            funcionario = em.find(Funcionario.class, funcionario.getId());
            if (funcionario != null) {
                em.remove(funcionario);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Funcionario> buscarPorSetor(Setor setor) {
        return em.createQuery("SELECT f FROM Funcionario f WHERE f.setor = :setor", Funcionario.class)
                .setParameter("setor", setor)
                .getResultList();
    }
}

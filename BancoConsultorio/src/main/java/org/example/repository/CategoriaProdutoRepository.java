package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.entities.CategoriaProduto;

import java.util.List;

public class CategoriaProdutoRepository {

    @PersistenceContext
    private EntityManager em; // O Jakarta/EE cuida da injeção do EntityManager


   public CategoriaProdutoRepository(EntityManager em) {
     this.em = em;
   }

    @Transactional
    public void salvar(CategoriaProduto categoriaProduto) {
        if (categoriaProduto.getId() == null) {
            em.persist(categoriaProduto);
        } else {
            em.merge(categoriaProduto);
        }
    }

    public CategoriaProduto buscarPorId(Long id) {
        return em.find(CategoriaProduto.class, id);
    }

    public List<CategoriaProduto> buscarTodos() {
        return em.createQuery("SELECT c FROM CategoriaProduto c", CategoriaProduto.class).getResultList();
    }

    @Transactional
    public void deletar(Long id) {
        CategoriaProduto categoria = em.find(CategoriaProduto.class, id);
        if (categoria != null) {
            em.remove(categoria);
        }
    }

    public void setEm(EntityManager em) {
    }
}

package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.entities.CategoriaProduto;
import org.example.entities.Produtos;

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


    @Transactional
    public void associarProdutos(Long categoriaId, List<Produtos> produtos) {
        CategoriaProduto categoria = em.find(CategoriaProduto.class, categoriaId);
        if (categoria != null) {
            for (Produtos produto : produtos) {
                categoria.getProdutos().add(produto);
                produto.getCategoriasProduto().add(categoria);
                em.merge(produto); // Atualiza o produto também
            }
            em.merge(categoria); // Atualiza a categoria
        }
    }

    @Transactional
    public void desvincularProdutos(Long categoriaId, List<Produtos> produtos) {
        CategoriaProduto categoria = em.find(CategoriaProduto.class, categoriaId);
        if (categoria != null) {
            for (Produtos produto : produtos) {
                categoria.getProdutos().remove(produto);
                produto.getCategoriasProduto().remove(categoria);
                em.merge(produto); // Atualiza o produto também
            }
            em.merge(categoria); // Atualiza a categoria
        }
    }


    public void setEm(EntityManager em) {
        this.em = em;
    }
}


package org.example.repository;

import jakarta.persistence.*;
import org.example.Service.CurvaABC;
import org.example.Service.LucroService;
import org.example.entities.AuditoriaVenda;
import org.example.entities.Produtos;
import org.example.entities.Usuario;

import java.util.List;

public class ProdutosRepository {

    private EntityManager em;

    public ProdutosRepository(EntityManager em) {
        this.em = em;
    }

    public void salvar(Produtos produto) {
        try {
            em.getTransaction().begin();
            em.persist(produto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    // Atualizado: método com lucro e auditoria
    public void registrarVenda(long idProduto, int quantidade, Usuario vendedor) {
        Produtos produto = em.find(Produtos.class, idProduto);
        if (produto != null) {
            if (produto.getEstoque() >= quantidade) {
                produto.setQuantidade_vendida(produto.getQuantidade_vendida() + quantidade);
                produto.setEstoque(produto.getEstoque() - quantidade);

                // 🟢 Registra o lucro da venda (30% do valor total vendido)
                double valorVenda = produto.getValor() * quantidade;
                LucroService.registrarLucro(valorVenda);

                try {
                    em.getTransaction().begin();

                    em.merge(produto);

                    // 🟢 Salva a venda na auditoria
                    AuditoriaVenda auditoria = new AuditoriaVenda();
                    auditoria.setProduto(produto);
                    auditoria.setVendedor(vendedor);
                    auditoria.setQuantidade(quantidade);
                    em.persist(auditoria);

                    em.getTransaction().commit();

                    aplicarCurvaABC();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    e.printStackTrace();
                }
            } else {
                System.out.println("Estoque insuficiente para a venda.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private void aplicarCurvaABC() {
        List<Produtos> produtos = em.createQuery("SELECT p FROM Produtos p", Produtos.class).getResultList();
        CurvaABC.classificar(produtos);
    }

    public List<Produtos> buscarTodos() {
        List<Produtos> produtos = em.createQuery("SELECT p FROM Produtos p", Produtos.class).getResultList();
        aplicarCurvaABCEmLista(produtos);
        return produtos;
    }

    public Produtos buscarPorId(long id) {
        Produtos produto = em.find(Produtos.class, id);
        aplicarCurvaABCEmProduto(produto);
        return produto;
    }

    private void aplicarCurvaABCEmLista(List<Produtos> produtos) {
        CurvaABC.classificar(produtos);
    }

    private void aplicarCurvaABCEmProduto(Produtos produto) {
        if (produto != null) {
            CurvaABC.classificar(List.of(produto));
        }
    }

    public void atualizarProdutos(List<Produtos> produtos) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Produtos produto : produtos) {
                em.merge(produto);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void adicionarEstoque(long idProduto, int quantidade) {
        Produtos produto = em.find(Produtos.class, idProduto);
        if (produto != null) {
            em.getTransaction().begin();
            produto.setEstoque(produto.getEstoque() + quantidade);
            em.merge(produto);
            em.getTransaction().commit();
            System.out.println("Estoque atualizado com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}

package org.example.Service;

import jakarta.persistence.EntityManager;
import org.example.entities.Produtos;
import org.example.repository.ProdutosRepository;

public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final EntityManager em;

    public ProdutosService(EntityManager em) {
        this.em = em;
        this.produtosRepository = new ProdutosRepository(em);
    }

    public boolean realizarVenda(Long idProduto, int quantidadeDesejada) {
        Produtos produto = produtosRepository.buscarPorId(idProduto);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return false;
        }

        if (produto.getEstoque() < quantidadeDesejada) {
            System.out.println("Erro: Estoque insuficiente para a venda.");
            return false;
        }

        // Atualiza estoque e quantidade vendida
        try {
            em.getTransaction().begin();
            produto.setEstoque(produto.getEstoque() - quantidadeDesejada);
            produto.setQuantidade_vendida(produto.getQuantidade_vendida() + quantidadeDesejada);
            em.merge(produto);
            em.getTransaction().commit();
            System.out.println("Venda realizada com sucesso.");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao realizar venda: " + e.getMessage());
            return false;
        }
    }
}
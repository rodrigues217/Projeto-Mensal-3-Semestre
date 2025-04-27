package org.example.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categoria_produto") // Nome da tabela no banco
public class CategoriaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "categoriasProduto")
    private Set<Produtos> produtos = new HashSet<>();

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Produtos> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produtos> produtos) {
        this.produtos = produtos;
    }
}
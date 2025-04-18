package org.example.Service;

import org.example.entities.Produtos;

import java.util.Comparator;
import java.util.List;

public class CurvaABC {
    public static List<Produtos> classificar(List<Produtos> produtos) {
        // 1. Calcular o valor de consumo de cada produto (preço * quantidade vendida)
        for (Produtos p : produtos) {
            double valorConsumo = p.getValor() * p.getQuantidade_vendida();
            p.setValorConsumo(valorConsumo);
        }

        // 2. Ordenar os produtos do maior para o menor valor de consumo
        produtos.sort(Comparator.comparingDouble(Produtos::getValorConsumo).reversed());

        // 3. Calcular o total do consumo
        double total = produtos.stream().mapToDouble(Produtos::getValorConsumo).sum();

        // 4. Se não houve consumo, todos são categoria C
        if (total == 0) {
            for (Produtos p : produtos) {
                p.setCategoria("C");
            }
            return produtos;
        }

        // 5. Calcular o percentual acumulado e classificar
        double acumulado = 0.0;

        for (Produtos p : produtos) {
            double percentualAcumulado = (acumulado / total) * 100;

            if (percentualAcumulado <= 70) {
                p.setCategoria("A");
            } else if (percentualAcumulado <= 90) {
                p.setCategoria("B");
            } else {
                p.setCategoria("C");
            }

            acumulado += p.getValorConsumo();
        }

        return produtos;
    }
}
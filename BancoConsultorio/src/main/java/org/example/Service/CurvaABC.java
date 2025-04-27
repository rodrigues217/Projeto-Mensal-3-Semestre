package org.example.Service;

import org.example.entities.Produtos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CurvaABC {
    public static List<Produtos> classificar(List<Produtos> produtos) {
        // 1. Calcular o valor de consumo de cada produto (preço * quantidade vendida)
        for (Produtos p : produtos) {
            double valorConsumo = p.getValor() * p.getQuantidade_vendida();
            p.setValorConsumo(valorConsumo);
        }

        // 2. Criar uma cópia mutável e ordenar
        List<Produtos> listaMutavel = new ArrayList<>(produtos);
        listaMutavel.sort(Comparator.comparingDouble(Produtos::getValorConsumo).reversed());

        // 3. Calcular o total do consumo
        double total = listaMutavel.stream().mapToDouble(Produtos::getValorConsumo).sum();

        // 4. Se não houve consumo, todos são categoria C
        if (total == 0) {
            for (Produtos p : listaMutavel) {
                p.setCategoria("C");
            }
            return listaMutavel;
        }

        // 5. Calcular o percentual acumulado e classificar
        double acumulado = 0.0;

        for (Produtos p : listaMutavel) {
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

        return listaMutavel;
    }
}
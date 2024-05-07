package org.example.pedido;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private double valorTotal;
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(Produto p, int qtd) {
        this.itens.add(new ItemPedido(p, qtd));
    }

    public double obterValorPedido() {
        if (itens.isEmpty()) {
            throw new IllegalArgumentException("Necessário ter pelo menos um item no pedido");
        }

        valorTotal = itens.stream()
                .mapToDouble(ItemPedido::obterValorItemProduto)
                .sum();
        return valorTotal;

    }

    public List<ItemPedido> getItens() {
        return itens;
    }
}
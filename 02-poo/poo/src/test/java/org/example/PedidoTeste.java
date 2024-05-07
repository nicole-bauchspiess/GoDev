package org.example;

import org.example.pedido.Pedido;
import org.example.pedido.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PedidoTeste {

    Pedido pedido;
    Produto produto1;
    Produto produto2;


    @BeforeEach
    public void setUp(){
        pedido = new Pedido();
        produto1 = new Produto(123, 1000, "computador");
        produto2 = new Produto(456, 2, "caneta");

    }

    @DisplayName("Inserção de itens no pedido")
    @Test
    public void testeAdicionaElementosLista(){
        pedido.adicionarItem(produto1, 10);
        pedido.adicionarItem(produto2, 100);
        Assertions.assertEquals(2,pedido.getItens().size());

    }

    @DisplayName("Valor total do pedido")
    @Test
    public void testeValorTotalPedido(){
        pedido.adicionarItem(produto1, 10);
        pedido.adicionarItem(produto2, 100);
        Assertions.assertEquals(10200,pedido.obterValorPedido());
    }

    @DisplayName("Valor do pedido com alteração do preço do produto")
    @Test //mesmo se mudar o valor do produto, o valor do pedido nao deve mudar- ok
    public void testeValorTotalPedidoTrocandoPrecoProduto(){
        pedido.adicionarItem(produto1, 10);
        pedido.adicionarItem(produto2, 100);
        produto2.setValor(45);
        Assertions.assertEquals(10200,pedido.obterValorPedido());
    }


    @DisplayName("Valor do produto = 0")
    @Test
    public void testeValorZero(){
        produto2 = new Produto(456, 0, "caneta");
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,  () -> pedido.adicionarItem(produto2, 10));
        Assertions.assertEquals("Valor do produto precisa ser maior que zero", exception.getMessage());

    }

    @DisplayName("Pedido com pelo menos um item")
    @Test
    public void testeValorTotalPedidoSemItens() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,  () -> pedido.obterValorPedido());
        Assertions.assertEquals("Necessário ter pelo menos um item no pedido", exception.getMessage());
    }

}

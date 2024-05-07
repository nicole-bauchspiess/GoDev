package org.example.pedido;

public class ItemPedido {

    private int qtde;
    private double valorProduto;

    public ItemPedido(Produto prod, int qtde){
        this.setQtde(qtde);
        this.valorProduto = prod.getValor();
    }

    public double obterValorItemProduto(){
        return valorProduto * qtde;
    }

    public void setQtde(int qtde) {
        if(qtde <= 0){
            throw new IllegalArgumentException("A quantidade do item deve ser maior do que zero");
        }
        this.qtde = qtde;
    }
}
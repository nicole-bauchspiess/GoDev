package org.example.pedido;

public class Produto {
    private int codigo;
    private double valor;
    private String descricao;

    Produto(){

    }

    public Produto(int codigo, double valor, String descricao) {
        this.setCodigo(codigo);
        this.setValor(valor);
        this.setDescricao(descricao);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if(codigo<=0){
            throw new IllegalArgumentException("O codigo do produto deve ser maior que zero");
        }
        this.codigo = codigo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do produto precisa ser maior que zero");
        }
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if(descricao == null || descricao.isBlank()){
            throw  new IllegalArgumentException("A descrição do produto está incorreta. Verifque");
        }
        this.descricao = descricao;
    }
}
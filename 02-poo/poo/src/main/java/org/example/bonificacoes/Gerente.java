package org.example.bonificacoes;

public class Gerente extends Funcionario {

    private String senha;
    private int qtdeGerenciados;

    public Gerente(String nome, String cpf, float salario, String senha, int qtde) {
        super(nome, cpf, salario);
        this.setSenha(senha);
        this.setQtdeGerenciados(qtde);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.validaNulo(senha);
        this.senha = senha;
    }

    public int getQtdeGerenciados() {
        return qtdeGerenciados;
    }

    public void setQtdeGerenciados(int qtdeGerenciados) {
        if(qtdeGerenciados <= 0){
            throw  new IllegalArgumentException("A quantidade de gerenciados deve ser maior que zero");
        }
        this.qtdeGerenciados = qtdeGerenciados;
    }

    @Override
    public float getBonificacao() {
        return this.getSalario() * 0.1f;
    }

}

package org.example.bonificacoes;

public class ControleBonificacoes {

    private double totalBonificacao;

    public void registra(Funcionario funcionario){
       totalBonificacao += funcionario.getBonificacao();
    }

    public double getTotalBonificacao(){
        return totalBonificacao;
    }
}

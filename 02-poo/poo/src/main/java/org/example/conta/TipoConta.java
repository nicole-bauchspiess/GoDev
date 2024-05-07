package org.example.conta;

public enum TipoConta {

    CONTA_CORRENTE(1),
    POUPANCA(2);

    private int indiceConta;


    TipoConta(int indiceConta) {
        this.indiceConta = indiceConta;
    }

    public int getIndice(){
        return indiceConta;
    }
}

package org.example.conta;

public class ContaBancaria {

    private String numero;
    private String agencia;
    private double saldo;
    private TipoConta tipoConta;

    public ContaBancaria(String numero, String agencia, double saldo, TipoConta tipoConta) {
        this.setNumero(numero);
        this.setAgencia(agencia);
        this.saldo = 0;
        this.setTipoConta(tipoConta);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        if(numero == null || !numero.matches("\\d{6}-\\d")){
            throw new IllegalArgumentException("O numero da conta dever ser XXXXXX-X");
        }
        this.numero = numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        if(agencia == null || !agencia.matches("\\d{4}")){
            throw new IllegalArgumentException("O numero da agência dever ser XXXX");
        }
        this.agencia = agencia;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        if(saldo <0){
            throw new IllegalArgumentException("O saldo não pode ser negativo");
        }
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        if(tipoConta== null){
            throw new IllegalArgumentException("O tipo da conta não pode ser nulo");
        }
        this.tipoConta = tipoConta;
    }

    //arrumar
    public boolean sacar(double valor){
        if(valor <=0){
            throw new IllegalArgumentException("Não é possível sacar um valor menor ou igual a zero");
        }

        if(this.tipoConta== TipoConta.CONTA_CORRENTE){
            valor+=0.5;
        }
        if(valor > saldo){
            return false;
        }
        this.saldo-=valor;
        return true;
    }

    public boolean depositar(double valor){
        if(valor <=0){
            throw new IllegalArgumentException("Não é possível depositar um valor menor ou igual a zero");
        }
        this.saldo+=valor;
        return true;
    }


}

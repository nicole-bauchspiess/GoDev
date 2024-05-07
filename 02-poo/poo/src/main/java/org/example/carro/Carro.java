package org.example.carro;

import java.time.LocalDate;

public class Carro {

    private String fabricante;
    private String modelo;
    private String cor;
    private int anoFabricacao;
    private double precoCompra;
    private Pessoa proprietario;

    public Carro() {

    }

    public Carro(String fabricante, String modelo, String cor, int anoFabricacao, double precoCompra, Pessoa proprietario) {
        this.setFabricante(fabricante);
        this.setModelo(modelo);
        this.setCor(cor);
        this.setAnoFabricacao(anoFabricacao);
        this.setPrecoCompra(precoCompra);
        this.proprietario = proprietario;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.validaNulo(fabricante);
        this.fabricante = fabricante;
    }


    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.validaNulo(modelo);
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.validaNulo(cor);
        this.cor = cor;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        if(anoFabricacao < 1886 || anoFabricacao > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("O ano de fabricação deve ser entre 1886 e o ano atual");
        }
        this.anoFabricacao = anoFabricacao;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        if(precoCompra <=0){
            throw new IllegalArgumentException("O valor de compra deve ser maior que zero");
        }
        this.precoCompra = precoCompra;
    }

    public Pessoa getProprietario() {
        return proprietario;
    }

    public void setProprietario(Pessoa proprietario) {
        this.proprietario = proprietario;
    }

    private void validaNulo(String texto) {
        if(texto == null || texto.isBlank()){
            throw new IllegalArgumentException("O campo deve ser preenchido");
        }
    }

    public double calcularIpva() {
        if (this.calcularTempoDeUsoEmAnos() > 10) {
            return 0.0;
        }
        return 0.04 * this.calcularValorRevenda();
    }

    public double calcularValorRevenda() {

        double valorVenda = precoCompra;
        for (int i = 0; i < this.calcularTempoDeUsoEmAnos(); i++) {
            valorVenda *= 0.95;
            if (i == 19) {
                return valorVenda;
            }
        }
        return valorVenda;
    }

    public int calcularTempoDeUsoEmAnos() {
        LocalDate ano = LocalDate.now();
        return ano.getYear() - anoFabricacao;
    }


}

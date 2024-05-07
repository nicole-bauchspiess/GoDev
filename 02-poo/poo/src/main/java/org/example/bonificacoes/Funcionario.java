package org.example.bonificacoes;

public class Funcionario {

    private String nome;
    private String cpf;
    private float salario;

    public Funcionario(String nome, String cpf, float salario) {
        this.setNome(nome);
        this.setCpf(cpf);
        this.setSalario(salario);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.validaNulo(nome);
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.validaNulo(cpf);
        this.cpf = cpf;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        if(salario <=0){
            throw new IllegalArgumentException("O salario deve ser maior que 0.0");
        }
        this.salario = salario;
    }

    public float getBonificacao(){
        return this.getSalario() * 0.05f;
    }

    public void validaNulo(String texto){
        if(texto == null || texto.isBlank()){
            throw new IllegalArgumentException("Campo deve ser preenchido");
        }
    }
}

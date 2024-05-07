package org.example.carro;

public class Pessoa{

    private String nome;
    private String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome ==null || nome.isBlank()){
            throw new IllegalArgumentException("O nome deve ser preenchido");
        }
        this.nome = nome;
    }

    public String getCpf() {

        return cpf;
    }

    public void setCpf(String cpf) {
        if(cpf ==null || cpf.isBlank()){
            throw new IllegalArgumentException("O CPF deve ser preenchido");
        }
        this.cpf = cpf;
    }
}
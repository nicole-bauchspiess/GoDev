package org.example.recursividade;

public class Recursividade {

    private int fatorial(int numero) {
        if (numero == 0 || numero == 1) {
            return 1;
        }
        numero *= fatorial(numero - 1);
        return numero;
    }


    private int somaPares(int numero) {
        if (numero % 2 != 0) {
            numero--;
        }
        if (numero == 0) {
            return numero;
        }
        numero += somaPares(numero - 2);

        return numero;
    }

    private int fibonnaci(int numero) {
        if (numero == 0 || numero == 1) {
            return numero;
        }
        return fibonnaci(numero - 1) + fibonnaci(numero - 2);
    }


    public int recursividade(int numero, String metodo) {
        if (numero < 0) {
            throw new IllegalArgumentException("Número negativo");
        }
        return switch (metodo) {
            case "fibonnaci" -> this.fibonnaci(numero);
            case "fatorial" -> this.fatorial(numero);
            case "somaPares" -> this.somaPares(numero);
            default -> throw new IllegalArgumentException("Expressao inesperada");
        };
    }

}

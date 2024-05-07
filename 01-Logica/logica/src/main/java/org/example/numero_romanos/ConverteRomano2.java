package org.example.numero_romanos;

import org.example.numero_romanos.exceptions.ConverteRomanoException;

public class ConverteRomano2 {

    private String valida(int numero, String casa) {
        String valor = "";
        if (casa.equals("unidade")) {
            valor = "I,V,X";
            numero = (((numero % 1000) % 100) % 10);
        } else if (casa.equals("dezena")) {
            valor = "X,L,C";
            numero = ((numero % 1000) % 100) / 10;
        } else if (casa.equals("centena")) {
            valor = "C,D,M";
            numero = (numero % 1000) / 100;
        } else {
            valor = "M,";
            numero = numero / 1000;
        }

        String[] casas = valor.split(",");

        if (numero < 4) {
            return casas[0].repeat(numero);
        } else if (numero == 4) {
            return casas[0] + casas[1];
        } else if (numero == 5) {
            return casas[1];
        } else if (numero < 9) {
            return casas[1] + casas[0].repeat(numero - 5);
        } else {
            return casas[0] + casas[2];
        }
    }

    private void validaNumero(int numero) {
        if (numero < 1 || numero > 3999) {
            throw new ConverteRomanoException("Valor deve ser entre 1 e 3999");
        }
    }

    public String converteRomano(int numero) {
        this.validaNumero(numero);
        return this.valida(numero, "milhares") + this.valida(numero, "centena")
                + this.valida(numero, "dezena") + this.valida(numero, "unidade");
    }


}

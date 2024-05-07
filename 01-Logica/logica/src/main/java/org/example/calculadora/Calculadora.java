package org.example.calculadora;

import java.math.BigDecimal;

public class Calculadora {


    public BigDecimal calcula(char operacao, String... numbers) {

        BigDecimal operation = new BigDecimal(numbers[0]);

        for (int i = 1; i < numbers.length; i++) {

            switch (operacao) {

                case '+':
                    operation = operation.add(new BigDecimal(numbers[i]));
                    break;
                case '-':
                    operation = operation.subtract(new BigDecimal(numbers[i]));
                    break;
                case '*':
                    operation = operation.multiply(new BigDecimal(numbers[i]));
                    break;
                case '/':

                    if(operation.equals(BigDecimal.ZERO) && numbers[i].equals("0")){
                        throw new ArithmeticException("Divisão indeterminada");
                    }
                    if(numbers[i].equals("0") && i!= 0) {
                        throw new ArithmeticException("Não é possível fazer divisão por 0");
                    }
                    operation = operation.divide(new BigDecimal(numbers[i]));
                    break;
                default: throw new IllegalArgumentException("Operacao invalida");
            }
        }
        return operation;
    }




}

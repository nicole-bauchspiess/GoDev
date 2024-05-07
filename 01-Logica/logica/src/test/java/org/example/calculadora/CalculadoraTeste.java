package org.example.calculadora;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTeste {

    Calculadora calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new Calculadora();
    }


    @Test
    void testeSomaComPositivos() {
        Assertions.assertEquals(new BigDecimal("1.22"), calculadora.calcula('+',"0.03","0.04", "0.7","0.45"));
    }

    @Test
    void testeSomaComNegativos() {
        Assertions.assertEquals(new BigDecimal("-8.448"), calculadora.calcula('+',"0","-9.598", "0.7","0.45"));
    }


    @Test
    void testeSubtracaoComPositivos() {
        Assertions.assertEquals(new BigDecimal("-0.71"), calculadora.calcula('-',"0.03","0.04", "0.7"));
    }

    @Test
    void testeSubtracaoComNegativos() {
        Assertions.assertEquals(new BigDecimal("8.448"), calculadora.calcula('-',"0","-9.598", "0.7","0.45"));
    }

    @Test
    void testeMultiplicacaoComPositivos() {
        Assertions.assertEquals(new BigDecimal("0.0003780"), calculadora.calcula('*',"0.03","0.04", "0.7","0.45"));
    }


    @Test
    void testeMultiplicacaoComNegativos() {
        Assertions.assertEquals(new BigDecimal("-1.121516"), calculadora.calcula('*',"-0.359","0.4", "7.1","1.1"));
    }

    @Test
    void testeDivisaooComNegativos() {
        Assertions.assertEquals(new BigDecimal("-1.3875"), calculadora.calcula('/',"-5.55", "2", "2"));
    }

    @Test
    void testeDivisaoComZeros() {
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class,
                () -> calculadora.calcula('/', "0", "1","0"));
        Assertions.assertEquals("Divisão indeterminada", exception.getMessage());
    }

    @Test
    void testeDivisaoComZero() {

        Calculadora calculadora = new Calculadora();
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class,
                () -> calculadora.calcula('/', "10.4","3.2","0"));
        Assertions.assertEquals("Não é possível fazer divisão por 0", exception.getMessage());
    }

}
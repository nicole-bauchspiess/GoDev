package org.example.recursividade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecursividadeTeste {

    Recursividade recursividade;

    @BeforeEach
    void setUp() {
        recursividade = new Recursividade();
    }

    @DisplayName("Teste fatorial")
    @ParameterizedTest
    @CsvSource({
            "5,120",
            "6,720",
            "10,3628800",
            "0,1",
            "1,1",
            "2,2",
            "3,6"
    })
    public void testeFatorial(int numero, int esperado) {
        Assertions.assertEquals(esperado, recursividade.recursividade(numero, "fatorial"));
    }

    @Test
    public void testeNumeroNegativo(){
        IllegalArgumentException re = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> recursividade.recursividade(-3,"somaPares"));
        Assertions.assertEquals("Número negativo", re.getMessage());
    }

    @Test
    public void testeMetodoInesperado(){
        IllegalArgumentException re = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> recursividade.recursividade(6,"Inesperado"));
        Assertions.assertEquals("Expressao inesperada", re.getMessage());
    }

    @DisplayName("Teste soma de pares ")
    @ParameterizedTest
    @CsvSource({
            "100,2550",
            "101,2550",
            "102, 2652",
            "10, 30",
            "5, 6",
            "0,0",
            "1,0",
            "2,2",
            "55, 756"
    })
    public void testeSomaPares(int numero, int esperado) {
        Assertions.assertEquals(esperado, recursividade.recursividade(numero, "somaPares"));
    }


    @DisplayName("Teste Fibonnaci")
    @ParameterizedTest
    @CsvSource({
            "8,21",
            "5,5",
            "9, 34",
            "10, 55",
            "11, 89",
            "1,1",
            "0,0"
    })
    public void testeFibonnaci(int numero, int esperado) {
        Assertions.assertEquals(esperado, recursividade.recursividade(numero, "fibonnaci"));
    }


}
package org.example.numero_romanos;

import org.example.numero_romanos.exceptions.ConverteRomanoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static org.junit.jupiter.api.Assertions.*;

class ConverteRomano2Teste {

    ConverteRomano2 conversor;

    @BeforeEach
    public void setUp(){
        conversor = new ConverteRomano2();
    }


    @DisplayName("Teste romanos")
    @ParameterizedTest
    @CsvSource({
            "1, I",
            "14, XIV",
            "34, XXXIV",
            "56, LVI",
            "72, LXXII",
            "90, XC",
            "92, XCII",
            "247, CCXLVII",
            "333, CCCXXXIII",
            "480, CDLXXX",
            "600, DC",
            "896, DCCCXCVI",
            "948, CMXLVIII",
            "999, CMXCIX",
            "1201, MCCI",
            "1500,MD",
            "1876, MDCCCLXXVI",
            "3007, MMMVII",
            "3257, MMMCCLVII",
            "3999, MMMCMXCIX"
    })
    public void converteRomano(int numero, String esperado){
        Assertions.assertEquals(esperado, conversor.converteRomano(numero));

    }

    @Test
    public void testeNegativo(){
        ConverteRomanoException exception = Assertions.assertThrows(ConverteRomanoException.class,
                () -> conversor.converteRomano(-50));

        Assertions.assertEquals("Valor deve ser entre 1 e 3999", exception.getMessage());

    }

}
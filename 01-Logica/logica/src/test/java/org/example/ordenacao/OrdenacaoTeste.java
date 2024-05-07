package org.example.ordenacao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrdenacaoTeste {

    Ordenacao ordenacao;

    @BeforeEach
    public void setUp(){
        ordenacao = new Ordenacao();
    }

    @Test
    public void testeOrdenacao(){
        Assertions.assertAll(
                () -> Assertions.assertEquals("[0, 3, 9, 10, 11]", ordenacao.ordena(3,0, 11,10,9)),
                () -> Assertions.assertEquals("[-4, -1, 0, 4, 35]", ordenacao.ordena(4,0, -1,35,-4)),
                () -> Assertions.assertEquals("[-9, -5, 1, 3, 9]", ordenacao.ordena(-9,9, -5,3,1))

        );

    }


}
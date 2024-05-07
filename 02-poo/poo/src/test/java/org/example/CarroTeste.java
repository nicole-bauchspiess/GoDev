package org.example;

import org.example.carro.Carro;
import org.example.carro.Pessoa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.time.LocalDate;


public class CarroTeste {

     DecimalFormat df = new DecimalFormat("0.00");
    Carro carro;
    Pessoa p;

    @BeforeEach
    void setUp() {
        p = new Pessoa("Nicole", "191.000.000-37");
    }

    //9 anos
    @DisplayName("Cálculo de anos - 9 anos")
    @Test
    public void testeCalculaAnos() {
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-9, 30000.00, p);
        Assertions.assertEquals(9, carro.calcularTempoDeUsoEmAnos());
    }

    @DisplayName("IPVA - 9 anos ")
    @Test
    public void testeCalculaIPVA(){
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-9, 30000.00, p);
        Assertions.assertEquals("756,30", df.format(carro.calcularIpva()));
    }

    @DisplayName("Valor revenda - 9 anos ")
    @Test
    public void testeCalculaRevenda(){
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-9, 30000.00, p);
        Assertions.assertEquals("18907,48", df.format(carro.calcularValorRevenda()));
    }



    @DisplayName("IPVA - maior que 10 anos ")
    @Test
    public void testeCalculaIPVA2(){
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-14, 30000.00,p);
        Assertions.assertEquals("0,00", df.format(carro.calcularIpva()));
    }

    @DisplayName("Valor revenda - 14 anos ")
    @Test
    public void testeCalculaRevenda2(){
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-14, 30000.00,p);
        Assertions.assertEquals("14630,25", df.format(carro.calcularValorRevenda()));
    }



    @DisplayName("Valor revenda - 20 anos ")
    @Test
    public void testeCalculaRevenda3(){
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-20, 30000.00,p);
        Assertions.assertEquals( "10754,58", df.format(carro.calcularValorRevenda()));
    }


    @DisplayName("Valor revenda - 21 anos ")
    @Test
    public void testeCalculaRevenda4(){
        carro = new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-21, 30000.00,p);
        Assertions.assertEquals( "10754,58", df.format(carro.calcularValorRevenda()));
    }

    @DisplayName("Valor de compra = 0")
    //validando excecoes do construtor:
    @Test
    public void testeValorZero(){
       IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()-21, 0,p));

       Assertions.assertEquals("O valor de compra deve ser maior que zero", exception.getMessage());

    }

    @DisplayName("Ano inválido")
    @Test
    public void testeAnoInvalido(){
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Carro("Fiat", "Argo", "Branca", 1800, 30000,p));

        Assertions.assertEquals("O ano de fabricação deve ser entre 1886 e o ano atual", exception.getMessage());

    }

    @DisplayName("Ano Maior que o atual")
    @Test
    public void testeAnoInvalido2(){
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Carro("Fiat", "Argo", "Branca", LocalDate.now().getYear()+2, 30000,p));

        Assertions.assertEquals("O ano de fabricação deve ser entre 1886 e o ano atual", exception.getMessage());

    }

    @DisplayName("Preenchimento de campo obrigatório")
    @Test
    public void testeModeloNaoPreenchido(){
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Carro("Fiat", " ", "Branca", 2000, 30000,p));

        Assertions.assertEquals("O campo deve ser preenchido", exception.getMessage());

    }

}

package org.example.conta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

    ContaBancaria contaBancaria;

    @Test
    public void testeNumeroContaValido(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);
        Assertions.assertEquals("123456-0", contaBancaria.getNumero());
    }

    @Test
    public void testeNumeroContaInvalido(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> new ContaBancaria("126-0", "0101", 0, TipoConta.CONTA_CORRENTE));

        Assertions.assertEquals("O numero da conta dever ser XXXXXX-X", exception.getMessage());
    }
    @Test
    public void testeNumeroAgenciaInvalido(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> new ContaBancaria("987654-0", "01", 0, TipoConta.CONTA_CORRENTE));

        Assertions.assertEquals("O numero da agência dever ser XXXX", exception.getMessage());
    }


    @DisplayName("Deposito conta corrente")
    @Test
    public void testeDeposito(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);
        contaBancaria.depositar(1000.50);
        Assertions.assertEquals(1000.50, contaBancaria.getSaldo(), 0.01);
    }

    @DisplayName("Deposito retorno")
    @Test
    public void testeDepositoRetorno(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);

        Assertions.assertTrue(contaBancaria.depositar(1000.50));
    }

    @DisplayName("Saque retorno")
    @Test
    public void testeSaqueRetorno(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);
        contaBancaria.depositar(1000.50);
        Assertions.assertTrue(contaBancaria.sacar(200));
    }

    @DisplayName("Saque retorno false")
    @Test
    public void testeSaqueRetornoFalse(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);
        contaBancaria.depositar(1000.50);
        Assertions.assertFalse(contaBancaria.sacar(1200));
    }


    @DisplayName("Deposito com valor negativo")
    @Test
    public void testeDepositoValorNegativo(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> contaBancaria.depositar(-1000.50));
        Assertions.assertEquals("Não é possível depositar um valor menor ou igual a zero", exception.getMessage());
    }

    @DisplayName("Operações com conta corrente")
    @Test
    public void testeSaqueContaCorrente(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.CONTA_CORRENTE);
        contaBancaria.depositar(1500.90);
        contaBancaria.depositar(200);
        contaBancaria.sacar(300.50);
        Assertions.assertEquals(1399.9, contaBancaria.getSaldo(),0.01 ); //retira 0,5 centavos por ser conta corrente
    }

    @DisplayName("Operações com conta poupança")
    @Test
    public void testeSaqueContaPoupanca(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.POUPANCA);
        contaBancaria.depositar(1500.90);
        contaBancaria.depositar(200);
        contaBancaria.sacar(300.50);
        Assertions.assertEquals(1400.4, contaBancaria.getSaldo(),0.01 ); //conta poupança nao tem taxa de saque
    }

    @DisplayName("Valor a ser sacado maior do que o saldo")
    @Test
    public void testeSaqueMaiorQueSaldo(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.POUPANCA);
        contaBancaria.depositar(1500.90);
        contaBancaria.depositar(200);
        contaBancaria.sacar(1900);
        Assertions.assertEquals(1700.90, contaBancaria.getSaldo(), 0.01);
    }

    @DisplayName("Valor a ser sacado negativo")
    @Test
    public void testeSaqueNegativo(){
        contaBancaria = new ContaBancaria("123456-0", "1234", 0, TipoConta.POUPANCA);
        contaBancaria.depositar(1500.90);
        contaBancaria.depositar(200);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> contaBancaria.sacar(-300));
        Assertions.assertEquals("Não é possível sacar um valor menor ou igual a zero", exception.getMessage());
    }







}
package org.example;

import org.example.bonificacoes.ControleBonificacoes;
import org.example.bonificacoes.Funcionario;
import org.example.bonificacoes.Gerente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ControleBonificacoesTeste {

    ControleBonificacoes bonificacoes;


    @BeforeEach
    public void setUp(){
        bonificacoes = new ControleBonificacoes();
        bonificacoes.registra(new Funcionario("Funcionario1", "191.000.000-00", 1500f)); //75
        bonificacoes.registra(new Gerente("Gerente", "123.456.789-00", 10000f,"123", 15 )); //1000
        bonificacoes.registra(new Funcionario("Funcionario2", "987.654.321.00", 1300f)); //65
        bonificacoes.registra(new Gerente("Gerente2", "951.753.741-99", 6000f, "456", 10)); //600

    }

    @DisplayName("Soma bonificações")
    @Test
    public void testeBonificacoes(){
        Assertions.assertEquals(1740, bonificacoes.getTotalBonificacao());
    }

    @DisplayName("Salario = 0")
    @Test
    public void testeSalario(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> new Funcionario("Funcionario2", "987.654.321.00", 0));

        Assertions.assertEquals("O salario deve ser maior que 0.0", exception.getMessage());
    }

    @DisplayName("Preenchimento de campo obrigatório")
    @Test
    public void testeNomeEmBranco(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> new Funcionario(" ", "987.654.321.00", 0));

        Assertions.assertEquals("Campo deve ser preenchido", exception.getMessage());
    }

    @DisplayName("Quantidade de gerenciados = 0")
    @Test
    public void testeQuantidadeGerenciados(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> new Gerente("Gerente5", "951.753.741-99", 6000f, "456", 0));

        Assertions.assertEquals("A quantidade de gerenciados deve ser maior que zero", exception.getMessage());
    }

}

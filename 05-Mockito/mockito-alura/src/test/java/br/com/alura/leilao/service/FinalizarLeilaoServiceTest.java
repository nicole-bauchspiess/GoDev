package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinalizarLeilaoServiceTest {


    private FinalizarLeilaoService service;

    @Mock
    private LeilaoDao leilaoDao;

    @Mock
    private EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    public void beforeEach(){
        MockitoAnnotations.initMocks(this); //pega anotações do mock desta classe e inicia eles
        this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
    }

    @Test
    public void deveriaFinalizarUmLeilao(){
        List<Leilao> leiloes = leiloes();

        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes); //força o mock a retornar leiloes e nao uma lista vazia
        Leilao leilao = leiloes.get(0);

        service.finalizarLeiloesExpirados();
        Assertions.assertTrue(leilao.isFechado());
        Assertions.assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());

        Mockito.verify(leilaoDao).salvar(leilao); //Chama o metodo verify para verificar se o mock (LeilaoDao) chamou o metodo salvar
    }

    @Test
    public void deveriaEnviarEmailParaVencedor(){
        List<Leilao> leiloes = leiloes();

        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

        service.finalizarLeiloesExpirados();
        Leilao leilao = leiloes.get(0);
       Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(leilao.getLanceVencedor()); //verifica se o enviadorDeEmail chamou o metodo enviarEmailVencedor para o lanceVencedor
    }

    @Test
    public void naoDeveriaEnviarEmailParaVencedorEmCasoDeErroAoEncerrarLeilao() {
        List<Leilao> leiloes = leiloes();

        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
        Mockito.when((leilaoDao).salvar(Mockito.any())).thenThrow(RuntimeException.class); //força exceção

        try {
            service.finalizarLeiloesExpirados();
            Mockito.verifyNoInteractions(enviadorDeEmails); //verifica se nao teve interações com essemetodo. Se deu erro no salvamento: nao envia email
        } catch (Exception e) {
        }
    }



    private List<Leilao> leiloes() {
        List<Leilao> lista = new ArrayList<>();

        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
        Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        lista.add(leilao);
        return lista;

    }


}
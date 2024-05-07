package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import java.math.BigDecimal;
import java.time.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class GeradorDePagamentoTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @Mock
    private Clock clock;

    @BeforeEach
    public void beforeEach() {
        initMocks(this);
        geradorDePagamento = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao() {
        Leilao leilao = leiloes();
        Lance lance = leilao.getLanceVencedor();
        lance.setLeilao(leilao);

        LocalDate data = LocalDate.of(2020, 12 ,7);

        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(lance);

        verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
        assertEquals(lance.getValor(), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(lance.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    void deveriaCriarPagamentoParaSegundaQuandoLanceForSexta() {
        Leilao leilao = leiloes();
        Lance lance = leilao.getLanceVencedor();
        lance.setLeilao(leilao);

        LocalDate data = LocalDate.of(2022, 11 ,11);

        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(lance);

        verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now(clock).plusDays(3), pagamento.getVencimento());
        assertEquals(lance.getValor(), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(lance.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    private Leilao leiloes() {
        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));
        Lance lance = new Lance(new Usuario("Beltrano"),
                new BigDecimal("600"));
        leilao.setLanceVencedor(lance);
        return leilao;
    }


}
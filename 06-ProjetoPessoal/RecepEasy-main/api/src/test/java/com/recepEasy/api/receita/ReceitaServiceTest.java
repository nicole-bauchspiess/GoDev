package com.recepEasy.api.receita;

import com.recepEasy.api.exceptions.NotFoundException;
import com.recepEasy.api.receita.dto.DadosAlterarReceita;
import com.recepEasy.api.receita.dto.DadosDetalhamentoReceita;
import com.recepEasy.api.usuario.Usuario;
import com.recepEasy.api.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceitaServiceTest {

    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private ReceitaRepository repository;

    @InjectMocks
    private ReceitaService service;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private DadosDetalhamentoReceita dadosDetalhamentoReceita;

    Usuario usuario;

    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        MockitoAnnotations.initMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("OK-> Deveria retornar uma receita")
    public void findById(){
        Long id = 1L;
        Receita receita = new Receita();
        when(repository.findById(id)).thenReturn(Optional.of(receita));
        assertThat(receita).isEqualTo(service.findById(id));
    }

    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao buscar uma receita que não existe")
    public void findById_receitaNaoEncontrada(){
        Long id = 1L;
        Receita receita = new Receita();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(()-> service.findById(id));
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("Receita 1 não encontrada");
    }

    @Test
    @DisplayName("OK-> Deveria atualizar a receita quando o usuário logado for quem fez a publicação da receita")
    public void atualizar(){

        DadosAlterarReceita dados = new DadosAlterarReceita(1L, "nova receita", "ingredientes", null, true);
        Long id = 1L;
        Receita receita = new Receita();
        receita.setUsuario(usuario);

        when(repository.findById(id)).thenReturn(Optional.of(receita));

        service.atualizar(dados);
        verify(repository).findById(id);

        assertEquals(receita.getNome(), "nova receita");
        assertEquals(receita.getIngredientes(), "ingredientes");
        assertNull(receita.getModoPreparo());
    }

    @Test
    @DisplayName(" Not OK-> Deveria lançar exceção ao tentar atualizar a receita de outro usuário")
    public void atualizar_usuarioIncorreto(){
        DadosAlterarReceita dados = new DadosAlterarReceita(1L, "nova receita", "ingredientes", null, true);
        Long id = 1L;
        Receita receita = new Receita();
        when(repository.findById(id)).thenReturn(Optional.of(receita));
        Throwable throwable = catchThrowable(()-> service.atualizar(dados));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Não é possível editar uma receita de outro usuário");
    }

    @Test
    @DisplayName("OK-> Deveria deletar a receita quando o usuário logado for quem fez a publicação da receita")
    public void deletar(){
        Long id = 1L;
        Receita receita = new Receita();
        receita.setUsuario(usuario);

        when(repository.findById(id)).thenReturn(Optional.of(receita));
        service.deletar(id);
        verify(repository).findById(id);

        assertFalse(receita.getAtivo());
    }

    @Test
    @DisplayName(" Not OK-> Deveria lançar exceção ao tentar deletar a receita de outro usuário")
    public void deletar_usuarioIncorreto(){
        Long id = 1L;
        Receita receita = new Receita();
        receita.setAtivo(true);
        when(repository.findById(id)).thenReturn(Optional.of(receita));

        Throwable throwable = catchThrowable(()-> service.deletar(id));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Não é possível editar uma receita de outro usuário");
        assertTrue(receita.getAtivo());
    }

    @Test
    @DisplayName("OK-> Deveria retornar todas as receitas cadastradas, mesmo as privadas quando o usuário for igual ao usuário logado")
    public void pegarReceitaDoUsuario(){
        Long id = 1L;

        when(usuarioService.findById(id)).thenReturn(usuario);

        Pageable page = PageRequest.of(2, 50);
        service.pegarReceitaDoUsuario(id, page);
        verify(repository).findReceitasByUsuario(usuario, page);
       // verifyNoInteractions(repository.findReceitasByUsuarioPrivadoFalse(usuario, page));

    }

    @Test
    @DisplayName("OK-> Deveria retornar somente as receitas publicas quando o usuário for diferente do logado")
    public void pegarReceitaDoUsuario_usuarioDiferente(){
        Long id = 1L;
        Usuario usuario1 = new Usuario();
        usuario1.setId(id);
        usuario.setId(2L);
        when(usuarioService.findById(id)).thenReturn(usuario1);

        Pageable page = PageRequest.of(2, 50);
        service.pegarReceitaDoUsuario(id, page);
        verify(repository).findReceitasByUsuarioPrivadoFalse(usuario1, page);
    }


    @Test
    @DisplayName("OK-> Deveria retornar a quantidade de curtidas")
    public void getQuantidadeCurtidas(){
        Receita receita = new Receita();
        receita.setQtdCurtidas(10);
        assertThat(service.getQuantidadeCurtidas(receita)).isEqualTo(10);
    }

    @Test
    public void getQuantidadeCurtidas_receitaNula(){
        Receita receita = null;
        Throwable throwable = catchThrowable(()-> service.getQuantidadeCurtidas(receita));
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("Não foi possível localizar a receita");
    }





}
package com.recepEasy.api.receita;

import com.recepEasy.api.interacao.InteracaoService;
import com.recepEasy.api.interacao.dto.DadosListagemInteracao;
import com.recepEasy.api.interacao.dto.DadosListagemInteracaoReceita;
import com.recepEasy.api.receita.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/receitas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Receita")
public class ReceitaController {

    @Autowired
    private ReceitaService service;
    @Autowired
    private InteracaoService interacaoService;


    @PostMapping
    @Operation(summary = "Cria uma receita")
    public ResponseEntity inserirReceita(@RequestBody @Valid DadosCadastroReceita dados) {
        Receita r = new Receita(dados);

        service.inserir(r);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(r).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoReceita(r));
    }

    @PostMapping("/gerar")
    @Operation(summary = "Cria uma receita via ChatGPT")
    public ResponseEntity inserirReceitaGPT(@RequestBody @Valid DadosCadastroReceitaGPT dados) {
        Receita r = service.receitaChatGPT(dados.ingredientes());
        service.inserir(r);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(r).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoReceita(r));
    }



    @GetMapping
    @Operation(summary = "Lista receitas ordenadas pela data de publicação")
    public Page<DadosDetalhamentoReceita> listarReceitas(
            @PageableDefault(size=50) Pageable paginacao) {
        return service.pegarReceitas(paginacao);

    }
    @GetMapping("/top/{pageSize}")
    @Operation(summary = "Lista receitas mais curtidas")
    public Page<DadosDetalhamentoReceita> listarReceitasMaisCurtidas(@PathVariable Integer pageSize) {
        return service.receitasMaisCurtidas(pageSize);
    }


    @PutMapping
    @Transactional
    @Operation(summary = "Atualiza uma receita")
    public ResponseEntity atualizarReceitas(@RequestBody @Valid DadosAlterarReceita dados) {
        Receita obj = service.atualizar(dados);
        return ResponseEntity.ok().body(new DadosDetalhamentoReceita(obj));
    }


    @DeleteMapping(value = "/{id}")
    @Transactional
    @Operation(summary = "Exclui logicamente uma receita")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Receita obj = service.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value="/usuario/{id}")
    @Operation(summary = "Lista as receitas de determinado usuário")
    public Page<DadosDetalhamentoReceita> listarReceitasUsuario(
      @PageableDefault(size = 20) Pageable paginacao, @PathVariable Long id) {
        return service.pegarReceitaDoUsuario(id, paginacao);

    }

    @GetMapping(value="/{id}/detalhes")
    @Operation(summary = "Lista as interações da receita")
    public Page<DadosListagemInteracaoReceita> listarInteracoesReceita(
            @PageableDefault(size = 50)
            @PathVariable Long id, Pageable pageable) {
        Receita receita = service.findById(id);
        return interacaoService.pegarInteracoesDaReceita(receita, pageable);

    }


}

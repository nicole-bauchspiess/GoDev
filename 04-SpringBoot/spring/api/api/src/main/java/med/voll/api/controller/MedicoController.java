package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.model.dto.DadosDetalhamentoMedico;
import med.voll.api.domain.model.dto.DadosAtualizacaoMedico;
import med.voll.api.domain.model.dto.DadosCadastroMedico;
import med.voll.api.domain.model.dto.DadosListagemMedico;
import med.voll.api.domain.model.Medico;
import med.voll.api.domain.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {


    @Autowired
    private MedicoRepository repository;


    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid  DadosCadastroMedico dados, UriComponentsBuilder builder){ //valid para validar os campos

        var medico = new Medico(dados);
        repository.save(medico);
        var uri = builder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    //se o parametro peageble nao for passado na url, ja carrega o deafault
    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listarMedicos(@PageableDefault(size =10, sort = {"nome"}) Pageable paginacao){ //pageable do spring.org.framework
        var page =repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new); //para cada medico na lista, ele cria um novo DadosListagem
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);

        return ResponseEntity.ok().body(new DadosDetalhamentoMedico(medico)); //retorna codigo 204 noContent
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok().body(new DadosDetalhamentoMedico(medico));
    }

    //exclusão fisica
//    @DeleteMapping("/{id}")
//    @Transactional
//    public void deletar(@PathVariable Long id){
//        repository.deleteById(id);
//
//    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build(); //retorna codigo 204 noContent
    }


}

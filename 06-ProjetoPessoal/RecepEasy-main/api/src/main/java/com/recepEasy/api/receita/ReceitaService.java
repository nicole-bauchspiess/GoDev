package com.recepEasy.api.receita;

import com.recepEasy.api.exceptions.NotFoundException;
import com.recepEasy.api.receita.dto.DadosAlterarReceita;
import com.recepEasy.api.receita.dto.DadosDetalhamentoReceita;

import com.recepEasy.api.usuario.Usuario;
import com.recepEasy.api.security.SecurityFilter;
import com.recepEasy.api.security.TokenService;
import com.recepEasy.api.chatGPT.ConsultaChatGPT;
import com.recepEasy.api.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository repository;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SecurityFilter filter;



    public void inserir(Receita obj) {
        Usuario usuario = getUsuario();
        obj.setUsuario(usuario);
        repository.save(obj);
    }


    public Receita atualizar(DadosAlterarReceita dados) {
        Receita receita = findById(dados.id());
        validaUsuario(receita);
        receita.atualizar(dados);
        return receita;
    }

    public Receita deletar(Long id) {
        Receita receita = findById(id);
        validaUsuario(receita);
        receita.excluir();
        return receita;
    }

    public Receita findById(Long id){
        Optional<Receita> receitaOptional = repository.findById(id);
        receitaOptional.orElseThrow(()-> new NotFoundException("Receita " +id+ " não encontrada"));
        return receitaOptional.get();
    }

    private void validaUsuario(Receita receita){
        Usuario usuario = getUsuario();
        if(!usuario.equals(receita.getUsuario())){
            throw new IllegalArgumentException("Não é possível editar uma receita de outro usuário");
        }
    }
    private Usuario getUsuario() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Page<DadosDetalhamentoReceita> pegarReceitas(Pageable paginacao) {
        return repository.findByAtivoTrueAndPrivadoFalseOrderByHoraPublicacaoDesc(paginacao);

    }


    public Page<DadosDetalhamentoReceita> pegarReceitaDoUsuario(Long id, Pageable paginacao){
        Usuario usuario = usuarioService.findById(id);
        Usuario usuarioLogado = getUsuario();

        if(usuarioLogado.equals(usuario)){ //se for o mesmo usuário: retorna todas as receitas independente se estiver privado ou nao
            return repository.findReceitasByUsuario(usuario, paginacao);
        }else{
            return repository.findReceitasByUsuarioPrivadoFalse(usuario, paginacao);
        }
    }


    public Receita receitaChatGPT(String ingredientes) {
        String textoComestivel = ConsultaChatGPT.validaComestivel(ingredientes);
        if(textoComestivel.toLowerCase().contains("nao")||textoComestivel.toLowerCase().contains("não")){
            throw new IllegalArgumentException("Não é possível gerar receita com ingredientes nao comestiveis");
        }
        String texto = ConsultaChatGPT.obterReceita(ingredientes);
        System.out.println(texto);

        int posIngredientes = texto.toLowerCase().indexOf("ingredientes");
        int posPreparo = texto.toLowerCase().indexOf("preparo:");
        int posSabor = texto.toLowerCase().indexOf("sabor");

        String nomeReceita = texto.substring(0, posIngredientes).replace("Nome: ", "").replace("\n", "");
        String ingredientes2 = texto.substring(posIngredientes, posPreparo).replace("Ingredientes:", "");
        String modoPreparo = texto.substring(posPreparo, posSabor).replace("Preparo:", "");
        String sabor = texto.substring(posSabor).replace("Sabor: ", "").split(" ")[0].toUpperCase().replace(".", "");
        Sabor saborEnum;

        try {
            saborEnum = Sabor.valueOf(sabor);
        }catch(Exception e){
            saborEnum = Sabor.NULO;
        }
        return new Receita(null, nomeReceita, ingredientes2, modoPreparo, saborEnum, true, true, LocalDateTime.now(),  0, null );
    }


    public int getQuantidadeCurtidas(Receita receita){
        if(receita== null){
            throw new NotFoundException("Não foi possível localizar a receita");
        }
        return receita.getQtdCurtidas();
    }

    public Page<DadosDetalhamentoReceita> receitasMaisCurtidas(int pageSize){
        Pageable paginacao = PageRequest.of(0, pageSize);
       return repository.findReceitasMaisCurtidas(pageSize, paginacao);
    }




}

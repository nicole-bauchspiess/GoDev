package com.recepEasy.api.usuario;

import com.recepEasy.api.endereco.Endereco;
import com.recepEasy.api.endereco.EnderecoService;
import com.recepEasy.api.exceptions.NotFoundException;
import com.recepEasy.api.receita.Receita;
import com.recepEasy.api.usuario.Usuario;
import com.recepEasy.api.usuario.UsuarioRepository;
import com.recepEasy.api.usuario.dto.DadosAlterarUsuario;
import com.recepEasy.api.usuario.dto.DadosCadastroUsuario;
import com.recepEasy.api.usuario.dto.DadosExibirUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EnderecoService enderecoService;

    public Usuario findById(Long id){
        Optional<Usuario> user = repository.findById(id);
        user.orElseThrow(()-> new NotFoundException("Usuario "+id+" não encontrado"));
        return user.get();
    }

    public Usuario findByLogin(String login){
        Optional<Usuario> usuarioOptional = repository.findByLogin(login);
        usuarioOptional.orElseThrow(()-> new NotFoundException("Não foi possível localizar o login"));
        return usuarioOptional.get();
    }

    public Usuario inserir(Usuario obj, DadosCadastroUsuario dadosUsuario){
        String cep = dadosUsuario.endereco().cep();
        String numero = dadosUsuario.endereco().numero();
        String complemento = dadosUsuario.endereco().complemento();

        Endereco endereco = enderecoService.criaEndereco(cep, numero, complemento);
        obj.setEndereco(endereco);

        obj.setSenha(new BCryptPasswordEncoder().encode(obj.getSenha()));
        obj.setAtivo(true);
        return repository.save(obj);
    }



    public Usuario deletar(Long id){
        Usuario usuarioLogado = getUsuario();
        Usuario usuario = findById(id);
        if(!usuarioLogado.equals(usuario)){
            throw new IllegalArgumentException("Não é possível editar outro usuário");
        }
        usuario.excluir();
        return usuario;
    }

    public Usuario atualizar(DadosAlterarUsuario dados){
        Usuario usuarioLogado = getUsuario();
        Usuario usuario = findById(dados.id());
        if(!usuarioLogado.equals(usuario)){
            throw new IllegalArgumentException("Não é possível editar outro usuário");
        }
        usuario.atualizar(dados);
        return usuario;
    }


    private Usuario getUsuario() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}

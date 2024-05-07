package com.recepEasy.api.autenticacao;

import com.recepEasy.api.usuario.Usuario;
import com.recepEasy.api.usuario.UsuarioService;
import com.recepEasy.api.usuario.dto.DadosLoginUsuario;
import com.recepEasy.api.security.DadosTokenJWT;
import com.recepEasy.api.security.TokenService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService service;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity efetuarLogin(@NotNull @RequestBody @Valid DadosLoginUsuario dados){
        Usuario usuario = usuarioService.findByLogin(dados.getLogin());
        if(!usuario.getAtivo()){
            throw new IllegalArgumentException("Não é possível logal com usuário inativo");
        }
        var token = new UsernamePasswordAuthenticationToken(dados.getLogin(), dados.getSenha());
        var authentication = manager.authenticate(token);
        var tokenJWT = service.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}

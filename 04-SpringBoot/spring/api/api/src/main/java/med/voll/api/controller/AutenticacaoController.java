package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.model.Usuario;
import med.voll.api.domain.model.dto.DadosAutenticacao;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados){
        var authenticationTokentoken = new UsernamePasswordAuthenticationToken(dados.login(),dados.senha()); //como se fosse um dto do proprio spring
       var authentication = manager.authenticate(authenticationTokentoken);

       var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal()); //autenthication.getPrincipal -> pega o usuario logado

       return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }


}

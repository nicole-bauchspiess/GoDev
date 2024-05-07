package med.voll.api.infra.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//sempre que tiver uma requisicao: aplica esse filtro. Apenas uma unica vez por requisicao
@Component //componente generico (nao é repositoy, controller, service...)
public class SecurityFilter extends OncePerRequestFilter {


    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;


    @Override //filterchain: cadeia de filtros
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //o envio do token é por meio de um cabeçalho authentication (header)
        var tokenJWT = recuperToken(request);
        //validar o token e pegar o subject (usuario) - > login da pessoa que tem esse token
        if(tokenJWT!= null) {
            var login = tokenService.getSubject(tokenJWT);
            //faz autenticação forçada p string afirmando que está autenticado porque o token está correto
            var usuario = repository.findByLogin(login);

            var authenticathion = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticathion); //força autenticação
        }
        filterChain.doFilter(request, response); //chama o proximo filtro ou seja, filtra e depois chama o controller

    }

    private String recuperToken(HttpServletRequest request) {
        var authorizathionHeader = request.getHeader("Authorization"); //nome = authorizathion
        if(authorizathionHeader!=null){
            return authorizathionHeader.replace("Bearer ", "");
        }
        return null;
    }
}

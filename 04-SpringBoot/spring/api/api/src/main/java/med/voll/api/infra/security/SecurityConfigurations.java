package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    //BEAN -> devolve um objeto para o spring
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        return http.csrf(csrf -> csrf.disable()) //desabilita proteção contra csrf pois nosso token ja faz isso
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //stateless: nao armazena o estado do usuario (se está logaod ou nao)
                .authorizeHttpRequests(req ->  {
                req.requestMatchers("/login").permitAll(); //usuario pode disparar o login sem estar autenticado
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**" ).permitAll();
                    req.anyRequest().authenticated();//todas as outras autenticaoes: valida
    }).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //seta que o nosso filtro se o token é valido  pre definido vem antes do filtro do spring está logado
                .build(); //constroi um objeto do tipo securityfilterchain
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
        //spring nao consegue criar sozinho um objeto de authenticationmanager: temos que fazer manualmente para instanciar no AutenticacaoController com o autowired
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//define que o tipo de criptografia hash da senha é o BCrypt
    }
}

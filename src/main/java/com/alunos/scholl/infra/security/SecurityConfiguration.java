package com.alunos.scholl.infra.security;

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
public class SecurityConfiguration {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(s ->
                    s.requestMatchers(HttpMethod.POST, "/login").permitAll()
                     .requestMatchers("/create-login", "/create-account",
                             "/create-turma", "/create-aluno", "/update-aluno-info",
                             "/change-aluno-turma", "/create-periodo", "/add-turma-in-periodo",
                             "/add-professor", "/create-materia", "/add-periodo-in-materia",
                             "/add-turma-in-materia")
                                .hasAuthority(Authorities.DONO.name)
                     .requestMatchers(
                            "/deslogin", "/my-turmas", "/get-alunos",
                             "/get-periodos", "/get-materias", "/get-materias-of-aluno/**",
                             "/get-aluno-infos", "/get-my-materias", "/get-materia-infos",
                             "/update-materia-infos"
                     ).authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

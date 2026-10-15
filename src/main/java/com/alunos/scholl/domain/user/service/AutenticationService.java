package com.alunos.scholl.domain.user.service;

import com.alunos.scholl.domain.user.dtos.request.LoginRequest;
import com.alunos.scholl.domain.user.dtos.response.LoginResponse;
import com.alunos.scholl.domain.user.exceptions.UserException;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.infra.security.SecurityFilter;
import com.alunos.scholl.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class AutenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(UUID.fromString(username))
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public ResponseEntity<LoginResponse> doLogin(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(UserException::new);

        var token = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
        var autentication = manager.authenticate(token);

        var jwt = tokenService.gerarToken((User) autentication.getPrincipal());

        ResponseCookie responseCookie = ResponseCookie.from(SecurityFilter.KEY_COOKIE_JWT, jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new LoginResponse("Usuário logado"));
    }
}

package com.alunos.scholl.domain.user.service;

import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import com.alunos.scholl.domain.user.dtos.request.LoginRequest;
import com.alunos.scholl.domain.user.dtos.response.LoginResponse;
import com.alunos.scholl.domain.user.exceptions.UserException;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponse> doLogin(@Valid LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(UserException::new);

        var token = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
        var autentication = manager.authenticate(token);

        return autenticateUser((User) autentication.getPrincipal());
    }

    public ResponseEntity<LoginResponse> createAccount(@Valid CreateAccountRequest createAccountRequest) {
        var user = User.builder()
                .nome(createAccountRequest.nome())
                .dataNascimento(createAccountRequest.dataNascimento())
                .sobrenome(createAccountRequest.sobrenome())
                .nome(createAccountRequest.nome())
                .senha(passwordEncoder.encode(createAccountRequest.senha()))
                .build();

        userRepository.save(user);

        return autenticateUser(user);
    }

    private ResponseEntity<LoginResponse> autenticateUser(User user) {
        var jwt = tokenService.gerarToken(user);

        ResponseCookie responseCookie = ResponseCookie.from(GetUserOnRequest.KEY_COOKIE_JWT, jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new LoginResponse("Usuário logado"));
    }
}

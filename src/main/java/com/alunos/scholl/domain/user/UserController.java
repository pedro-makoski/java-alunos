package com.alunos.scholl.domain.user;

import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import com.alunos.scholl.domain.user.dtos.request.LoginRequest;
import com.alunos.scholl.domain.user.dtos.response.LoginResponse;
import com.alunos.scholl.domain.user.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        return loginService.doLogin(loginRequest);
    }

    @PostMapping("/create-account")
    public ResponseEntity<LoginResponse> createAccount(CreateAccountRequest createAccountRequest) {
        return loginService.createAccount(createAccountRequest);
    }


    @PostMapping("/deslogin")
    public ResponseEntity<?> deslogin() {
        loginService.deslogin();
    }
}

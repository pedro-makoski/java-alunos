package com.alunos.scholl.domain.user;

import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import com.alunos.scholl.domain.user.dtos.request.LoginRequest;
import com.alunos.scholl.domain.user.dtos.response.LoginResponse;
import com.alunos.scholl.domain.user.dtos.response.MeResponse;
import com.alunos.scholl.domain.user.service.LoginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        return loginService.doLogin(loginRequest);
    }

    @PostMapping("/create-account")
    @Transactional
    public ResponseEntity<LoginResponse> createAccount(
            @RequestBody CreateAccountRequest createAccountRequest) {
        return loginService.createAccount(createAccountRequest);
    }


    @DeleteMapping("/deslogin")
    public ResponseEntity<?> deslogin() {
        return loginService.deslogin();
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public MeResponse me(HttpServletRequest httpServletRequest) {
        return loginService.me(httpServletRequest);
    }
}

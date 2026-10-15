package com.alunos.scholl.domain.user.service;

import com.alunos.scholl.domain.user.exceptions.UserNotLogged;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.infra.security.TokenService;
import com.alunos.scholl.infra.utils.SearchForCookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class GetUserOnRequest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public User realizeWithThrow(HttpServletRequest httpServletRequest) {
        return realize(httpServletRequest)
                .orElseThrow(UserNotLogged::new);
    }

    public Optional<User> realize(HttpServletRequest httpServletRequest) {
        var tokenJWT = getJWTToken(httpServletRequest);

        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);

            return userRepository.findById(UUID.fromString(subject));
        }

        return Optional.empty();
    }

    public static String KEY_COOKIE_JWT = "jwt-login";

    private String getJWTToken(HttpServletRequest request) {
        return SearchForCookie.obterCookieHttpOnly(request, KEY_COOKIE_JWT);
    }
}

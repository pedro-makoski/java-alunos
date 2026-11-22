package com.alunos.scholl.infra.security;

import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private static final List<String> PUBLIC_PATHS = List.of(
            "/user/login",
            "/user/create-account",
            "/v3/api-docs",
            "/swagger-ui"
    );

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var userOpt = getUserOnRequest.realize(request);

        userOpt.ifPresent((user) -> {
            var authentication = new UsernamePasswordAuthenticationToken(
                    user, user.getPassword(),
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}

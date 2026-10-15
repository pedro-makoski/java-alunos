package com.alunos.scholl.domain.user.dtos.request;

public record LoginRequest(
        String email,
        String senha
) {
}

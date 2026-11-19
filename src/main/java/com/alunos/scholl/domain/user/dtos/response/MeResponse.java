package com.alunos.scholl.domain.user.dtos.response;

import com.alunos.scholl.domain.user.model.User;

public record MeResponse(
        String nome,
        String sobrenome,
        Boolean isTeacher
) {
    public MeResponse(User user) {
        this(user.getNome(), user.getSobrenome(), user.isTeacher());
    }
}

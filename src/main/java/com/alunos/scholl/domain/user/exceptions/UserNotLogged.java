package com.alunos.scholl.domain.user.exceptions;

public class UserNotLogged extends RuntimeException {
    public UserNotLogged() {
        super("Usuário não está loggado");
    }
}

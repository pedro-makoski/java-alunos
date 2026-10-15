package com.alunos.scholl.domain.user.exceptions;

public class UserException extends RuntimeException {
    public UserException() {
        super("Usuário ou senhas não encontrados");
    }
}

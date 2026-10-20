package com.alunos.scholl.domain.user.exceptions;

public class EmailAlreadyUsed extends RuntimeException {
    public EmailAlreadyUsed() {
        super("Esse email já está em uso");
    }
}

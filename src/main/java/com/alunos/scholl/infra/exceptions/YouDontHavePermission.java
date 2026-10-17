package com.alunos.scholl.infra.exceptions;

public class YouDontHavePermission extends RuntimeException {
    public YouDontHavePermission() {
        super("Você não tem permissão para realizar essa operação");
    }
}

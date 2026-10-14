package com.alunos.scholl.infra.security;

public enum Authorities {
    PROFESSOR("PROFESSOR"),
    DONO("DONO");

    public String name;

    Authorities(String name) {
        this.name = name;
    }
}

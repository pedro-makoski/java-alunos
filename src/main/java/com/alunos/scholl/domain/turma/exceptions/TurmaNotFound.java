package com.alunos.scholl.domain.turma.exceptions;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class TurmaNotFound extends NotFoundException {
    public TurmaNotFound() {
        super("Turma não encontrada");
    }
}

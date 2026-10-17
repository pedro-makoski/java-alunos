package com.alunos.scholl.domain.periodo.exceptions;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class PeriodoNotFound extends NotFoundException {
    public PeriodoNotFound() {
        super("Período não encontrado");
    }
}

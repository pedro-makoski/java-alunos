package com.alunos.scholl.domain.materia.exception;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class MateriaNotFound extends NotFoundException {
    public MateriaNotFound() {
        super("Materia não encontrada");
    }
}

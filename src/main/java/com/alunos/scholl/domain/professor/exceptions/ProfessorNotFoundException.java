package com.alunos.scholl.domain.professor.exceptions;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class ProfessorNotFoundException extends NotFoundException {
    public ProfessorNotFoundException() {
        super("Professor não encontrado");
    }
}

package com.alunos.scholl.domain.aluno.exceptions;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class AlunoNotFound extends NotFoundException {
    public AlunoNotFound() {
        super("Aluno não encontrado");
    }
}

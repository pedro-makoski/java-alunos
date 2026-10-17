package com.alunos.scholl.domain.aluno.exceptions;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class AlunoNotaNotFound extends NotFoundException {
    public AlunoNotaNotFound() {
        super("Aluno não encontrado");
    }
}

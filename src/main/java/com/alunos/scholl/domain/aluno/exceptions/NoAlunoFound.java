package com.alunos.scholl.domain.aluno.exceptions;

import com.alunos.scholl.infra.exceptions.NotFoundException;

public class NoAlunoFound extends NotFoundException {
    public NoAlunoFound() {
        super("Nenhum aluno encontrado");
    }
}

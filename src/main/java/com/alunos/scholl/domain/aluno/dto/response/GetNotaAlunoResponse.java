package com.alunos.scholl.domain.aluno.dto.response;


import com.alunos.scholl.domain.aluno.model.DetailsNota;
import com.alunos.scholl.domain.aluno.model.NotaAluno;

import java.util.List;

public record GetNotaAlunoResponse(
    Double nota,
    List<DetailsNota> detailsNota
) {

    public GetNotaAlunoResponse(NotaAluno notaAluno) {
        this(notaAluno.getNota(), notaAluno.getDetails());
    }
}

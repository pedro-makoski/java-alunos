package com.alunos.scholl.domain.aluno.dto.response;

import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.model.Aluno;

import java.time.LocalDate;
import java.util.UUID;

public record AlunoSample(
    UUID idAluno,
    String nome,
    String nomePai,
    String nomeMae,
    LocalDate dataNascimento
) {
    public AlunoSample(Aluno aluno) {
        this(aluno.getId(), aluno.getNome(),
                aluno.getNomePai(), aluno.getNomeMae(),
                aluno.getDataNascimento());
    }

    public AlunoSample(CreateAlunoRequest createAlunoRequest, UUID id) {
        this(id, createAlunoRequest.nome(), createAlunoRequest.nomePai(),
                createAlunoRequest.nomeMae(), createAlunoRequest.dataNascimento());
    }
}

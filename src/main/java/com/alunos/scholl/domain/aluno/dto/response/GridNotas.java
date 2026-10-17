package com.alunos.scholl.domain.aluno.dto.response;

import com.alunos.scholl.domain.aluno.model.DetailsNota;
import com.alunos.scholl.domain.aluno.model.NotaAluno;

import java.util.List;
import java.util.UUID;

public record GridNotas(
    List<UserItem> userItemList
) {

    public record UserItem(
            UUID id,
            String nome,
            Double nota,
            List<DetailsNota> detailsNota
    ) {
        public UserItem(NotaAluno notaAluno) {
            this(
                    notaAluno.getNotaAlunoId().getAluno().getId(),
                    notaAluno.getNotaAlunoId().getAluno().getNome(),
                    notaAluno.getNota(),
                    notaAluno.getDetails()
            );
        }
    }
}

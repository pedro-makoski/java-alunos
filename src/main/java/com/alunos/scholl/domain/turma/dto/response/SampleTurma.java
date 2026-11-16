package com.alunos.scholl.domain.turma.dto.response;

import com.alunos.scholl.domain.turma.model.TipoTurma;
import com.alunos.scholl.domain.turma.model.Turma;

import java.util.UUID;

public record SampleTurma(
        UUID id,
        String nome,
        TipoTurma tipoTurma,
        Long tamanhoMaximoTurma
) {
    public SampleTurma(Turma turma) {
        this(turma.getId(), turma.getNome(),
                turma.getTipo(), turma.getTamanhoMaximo());
    }
}

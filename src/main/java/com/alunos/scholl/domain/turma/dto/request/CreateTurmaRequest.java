package com.alunos.scholl.domain.turma.dto.request;

import com.alunos.scholl.domain.turma.model.TipoTurma;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateTurmaRequest (
        @Size(min = 3, max = 250, message = "O nome deve entre 3 e 250 caracteres")
        String nome,

        TipoTurma tipoTurma,

        @Min(value = 0, message = "A sala não pode ter quantidade negativa")
        Long tamanhoMaximo
) {}

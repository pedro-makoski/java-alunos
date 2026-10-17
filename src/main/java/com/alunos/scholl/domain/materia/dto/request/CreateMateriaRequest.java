package com.alunos.scholl.domain.materia.dto.request;

import java.util.UUID;

public record CreateMateriaRequest(
        String nome,
        UUID professorId,
        UUID turmaId
) {
}

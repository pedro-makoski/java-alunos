package com.alunos.scholl.domain.materia.dto.request;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateMateriaRequest(
        @Size(min = 2, max = 250, message = "O nome do usuário deve ter entre e 2 e 250 caracteres")
        String nome,
        UUID professorId,
        UUID turmaId
) {
}

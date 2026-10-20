package com.alunos.scholl.domain.materia.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateMateriaRequest(
        @Size(min = 2, max = 250, message = "O nome do usuário deve ter entre e 2 e 250 caracteres")
        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @NotNull(message = "O id do professor não pode ser nulo")
        UUID professorId,

        @NotNull(message = "O id da turma não pode ser nulo")
        UUID turmaId
) {
}

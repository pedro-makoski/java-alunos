package com.alunos.scholl.domain.professor.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PutProfessorRequest(
        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @NotNull(message = "O sobrenome não pode ser nulo")
        String sobrenome,

        @NotNull(message = "A data de nascimento não pode ser nulo")
        @Past
        LocalDate dataNascimento
) {
}

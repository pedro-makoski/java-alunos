package com.alunos.scholl.domain.professor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateProfessorRequest(
        @NotNull(message = "O email não pode ser nulo")
        String email,

        @NotNull(message = "S senha não pode ser nula")
        String senha,

        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @NotNull(message = "O sobrenome não pode ser nulo")
        String sobrenome,

        @NotNull(message = "A data de nascimento não pode ser nulo")
        LocalDate dataNascimento
) {
}

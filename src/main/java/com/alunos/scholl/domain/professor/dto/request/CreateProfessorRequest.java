package com.alunos.scholl.domain.professor.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateProfessorRequest(
        @NotNull(message = "O email não pode ser nulo")
        String email,

        @NotNull(message = "A senha não pode ser nula")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,

        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @NotNull(message = "O sobrenome não pode ser nulo")
        String sobrenome,

        @NotNull(message = "A data de nascimento não pode ser nulo")
        @Past
        LocalDate dataNascimento
) {
}

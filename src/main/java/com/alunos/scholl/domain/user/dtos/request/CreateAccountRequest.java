package com.alunos.scholl.domain.user.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAccountRequest(
        @Email(message = "O email não parece ser um email")
        @NotNull(message = "O email não pode ser nulo")
        String email,

        @Size(min = 8, max = 250, message = "A sua senha deve entre 8 e 250 caracteres")
        @NotNull(message = "A senha não pode ser nula")
        String senha,

        @PastOrPresent(message = "Você não pode ter nascido no futuro")
        @NotNull(message = "A data de nascimento não pode ser nula")
        LocalDate dataNascimento,

        @Size(min = 2, max = 250, message = "O seu nome deve ter entre 2 e 250 caracteres")
        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @Size(min = 2, max = 250, message = "O seu sobrenome deve ter entre 2 e 250 caracteres")
        @NotNull(message = "O sobrenome não pode ser nulo")
        String sobrenome
) {
}

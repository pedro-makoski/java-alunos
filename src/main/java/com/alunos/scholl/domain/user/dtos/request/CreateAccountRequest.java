package com.alunos.scholl.domain.user.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAccountRequest(
        @Email(message = "O email não parece ser um email")
        String email,

        @Size(min = 8, max = 250, message = "A sua senha deve entre 8 e 250 caracteres")
        String senha,

        @PastOrPresent(message = "Você não pode ter nascido no futuro")
        LocalDate dataNascimento,

        @Size(min = 2, max = 250, message = "O seu nome deve ter entre 2 e 250 caracteres")
        String nome,

        @Size(min = 2, max = 250, message = "O seu sobrenome deve ter entre 2 e 250 caracteres")
        String sobrenome
) {
}

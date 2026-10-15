package com.alunos.scholl.domain.user.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAccountRequest(
        @Email
        String email,

        @Size(min = 8, max = 250)
        String senha,

        @PastOrPresent
        LocalDate dataNascimento,

        @Size(min = 3, max = 250)
        String nome,

        @Size(min = 3, max = 250)
        String sobrenome
) {
}

package com.alunos.scholl.domain.aluno.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PutAlunoRequest(

        @Size(min = 2, max = 250, message = "O nome deve ter entre 2 e 250 caracteres")
        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @PastOrPresent
        @NotNull(message = "A data de nascimento não pode ser nula")
        LocalDate dataNascimento,

        @Size(min = 2, max = 250, message = "O nome do pai deve ter entre 2 e 250 caracteres")
        @NotNull(message = "O nome do pai não pode ser nulo")
        String nomePai,

        @Size(min = 2, max = 250, message = "O nome da mãe deve ter entre 2 e 250 caracteres")
        @NotNull(message = "O nome da mãe não pode ser nula")
        String nomeMae
) {
}

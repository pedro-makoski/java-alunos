package com.alunos.scholl.domain.aluno.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAlunoRequest(
    UUID idTurma,

    @Size(min = 2, max = 250, message = "O nome deve ter entre 2 e 250 caracteres")
    String nome,

    @PastOrPresent
    LocalDate dataNascimento,

    @Size(min = 2, max = 250, message = "O nome do pai deve ter entre 2 e 250 caracteres")
    String nomePai,

    @Size(min = 2, max = 250, message = "O nome da mãe deve ter entre 2 e 250 caracteres")
    String nomeMae
) {
}

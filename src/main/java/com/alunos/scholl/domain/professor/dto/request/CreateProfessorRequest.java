package com.alunos.scholl.domain.professor.dto.request;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record CreateProfessorRequest(
        String email,
        String senha,
        String nome,
        LocalDate dataNascimento,
        String sobrenome
) {
}

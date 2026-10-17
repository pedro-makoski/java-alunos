package com.alunos.scholl.domain.professor.dto.response;

import com.alunos.scholl.domain.professor.model.Professor;

import java.time.LocalDate;
import java.util.UUID;

public record SampleProfessor(
        UUID idProfessor,
        String nome,
        String sobrenome,
        LocalDate dataNascimento,
        String email
) {
    public SampleProfessor(Professor professor) {
        this(professor.getId(), professor.getUserRelated().getNome(),
                professor.getUserRelated().getSobrenome(),
                professor.getUserRelated().getDataNascimento(),
                professor.getUserRelated().getEmail());
    }
}

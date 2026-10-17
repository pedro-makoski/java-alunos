package com.alunos.scholl.domain.materia.dto.response;

import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.professor.dto.response.SampleProfessor;
import com.alunos.scholl.domain.turma.dto.response.SampleTurma;

import java.util.UUID;

public record MateriaSample(
        UUID idMateria,
        String nome,
        SampleProfessor sampleProfessor,
        SampleTurma sampleTurma
) {

    public MateriaSample(Materia materia) {
        this(materia.getId(), materia.getNome(),
                new SampleProfessor(materia.getProfessor()),
                new SampleTurma(materia.getTurma()));
    }
}

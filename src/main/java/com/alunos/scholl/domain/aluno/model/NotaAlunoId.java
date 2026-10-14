package com.alunos.scholl.domain.aluno.model;

import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.periodo.model.Periodo;
import jakarta.persistence.*;

import java.util.UUID;

@Embeddable
public class NotaAlunoId {
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "materia_id")
    Materia materia;
}

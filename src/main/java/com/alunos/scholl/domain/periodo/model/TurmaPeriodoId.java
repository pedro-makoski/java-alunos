package com.alunos.scholl.domain.periodo.model;

import com.alunos.scholl.domain.turma.model.Turma;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class TurmaPeriodoId {
    @ManyToOne
    @JoinColumn(name = "periodo_id")
    Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    Turma turma;
}

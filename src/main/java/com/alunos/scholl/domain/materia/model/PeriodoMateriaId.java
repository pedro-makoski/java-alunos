package com.alunos.scholl.domain.materia.model;

import com.alunos.scholl.domain.periodo.model.Periodo;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class PeriodoMateriaId {
    @ManyToOne
    @JoinColumn(name = "materia_id")
    Materia materia;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    Periodo periodo;

    public PeriodoMateriaId(Materia materia, Periodo periodo) {
        this.materia = materia;
        this.periodo = periodo;
    }
}

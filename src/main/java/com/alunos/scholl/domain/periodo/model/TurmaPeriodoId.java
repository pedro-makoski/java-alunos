package com.alunos.scholl.domain.periodo.model;

import com.alunos.scholl.domain.turma.model.Turma;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class TurmaPeriodoId {
    @ManyToOne
    @JoinColumn(name = "periodo_id")
    Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    Turma turma;

    public TurmaPeriodoId(Periodo periodo, Turma turma) {
            this.periodo = periodo;
            this.turma = turma;
    }
}

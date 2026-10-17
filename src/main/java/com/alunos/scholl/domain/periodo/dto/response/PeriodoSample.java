package com.alunos.scholl.domain.periodo.dto.response;

import com.alunos.scholl.domain.periodo.model.Periodo;
import com.alunos.scholl.domain.periodo.model.TipoPeriodo;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public record PeriodoSample(
        UUID idPeriodo,
        LocalDate inicio,
        LocalDate fim,
        TipoPeriodo tipoPeriodo
) {
    public PeriodoSample(Periodo periodo) {
        this(periodo.getId(), periodo.getInicio(),
                periodo.getFim(), periodo.getTipo());
    }
}

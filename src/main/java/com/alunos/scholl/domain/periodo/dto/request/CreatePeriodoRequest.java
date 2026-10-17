package com.alunos.scholl.domain.periodo.dto.request;

import com.alunos.scholl.domain.periodo.model.TipoPeriodo;

import java.time.LocalDate;

public record CreatePeriodoRequest(
        TipoPeriodo tipoPeriodo,
        LocalDate inicio,
        LocalDate fim
) {

}

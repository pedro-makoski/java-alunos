package com.alunos.scholl.domain.periodo.dto.request;

import com.alunos.scholl.domain.periodo.model.TipoPeriodo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PutPeriodoRequest(
        @NotNull(message = "O tipo do periodo não pode ser nulo")
        TipoPeriodo tipoPeriodo,

        @NotNull(message = "A date de inicio não pode ser nula")
        LocalDate inicio,

        @NotNull(message = "A data de fim não pode ser nula")
        LocalDate fim
) {

}

package com.alunos.scholl.domain.aluno.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DetailsNota(
        String description,

        @DecimalMin(value = "0.00", message = "A nota não pode ser negativa")
        @DecimalMax(value = "100.00", message = "A nota não pode ser maior que 100")
        Double nota
) {
}

package com.alunos.scholl.domain.aluno.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DetailsNota(
        String description,

        @Min(value = 0, message = "A nota não pode ser negativa")
        @Max(value = 100, message = "A nota não pode ser maior que 100")
        Double nota
) {
}

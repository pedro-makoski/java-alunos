package com.alunos.scholl.domain.aluno.dto.request;

import com.alunos.scholl.domain.aluno.model.DetailsNota;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record AddNotaRequest(
        @DecimalMin(value = "0.00", message = "A nota não pode ser menor que 0")
        @DecimalMax(value = "100.00", message = "A nota não pode ser maior que 100")
        Double nota,

        List<@Valid DetailsNota> detailsNota,

        UUID idAluno,
        UUID idPeriodo,
        UUID materiaId
) {
}

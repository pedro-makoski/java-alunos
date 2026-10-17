package com.alunos.scholl.domain.aluno.dto.response;

import java.util.Map;
import java.util.UUID;

public record AlunoDetails(
    UUID idAluno,
    Double notaMedia,
    Map<String, Double> materiaAndPeriodoNotas,
    String nomeAluno
) {

}

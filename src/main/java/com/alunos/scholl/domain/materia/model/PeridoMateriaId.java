package com.alunos.scholl.domain.materia.model;

import com.alunos.scholl.domain.periodo.model.Periodo;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Embeddable
public class PeridoMateriaId {
    @ManyToOne
    @JoinColumn(name = "materia_id")
    Materia materia;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    Periodo periodo;
}

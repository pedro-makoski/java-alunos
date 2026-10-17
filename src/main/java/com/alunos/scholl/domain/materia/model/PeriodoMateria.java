package com.alunos.scholl.domain.materia.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.periodo.model.Periodo;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PeriodoMateria extends BaseEntity {
    @EmbeddedId
    PeriodoMateriaId periodoMateriaId;

    public PeriodoMateria(Materia materia, Periodo periodo) {
        this.periodoMateriaId = new PeriodoMateriaId(materia, periodo);
    }
}

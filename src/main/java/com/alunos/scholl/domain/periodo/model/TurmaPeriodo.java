package com.alunos.scholl.domain.periodo.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.turma.model.Turma;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurmaPeriodo extends BaseEntity {
    @EmbeddedId
    TurmaPeriodoId turmaPeriodoId;

    public TurmaPeriodo(Turma turma, Periodo periodo) {
        this.turmaPeriodoId = new TurmaPeriodoId(periodo, turma);
    }
}

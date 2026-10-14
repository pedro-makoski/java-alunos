package com.alunos.scholl.domain.periodo.model;

import com.alunos.scholl.domain.BaseEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TurmaPeriodo extends BaseEntity {
    @EmbeddedId
    TurmaPeriodoId turmaPeriodoId;
}

package com.alunos.scholl.domain.aluno.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.periodo.model.Periodo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "nota_aluno_periodo_materia")
@Getter
@Setter
@NoArgsConstructor
public class NotaAluno extends BaseEntity {
    Double nota;

    @EmbeddedId
    NotaAlunoId notaAlunoId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb")
    DetailsNota details;
}

package com.alunos.scholl.domain.aluno.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.periodo.model.Periodo;
import com.alunos.scholl.domain.turma.model.Turma;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

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
    List<DetailsNota> details;

    public NotaAluno(AddNotaRequest addNotaRequest,
                     Aluno aluno,
                     Periodo periodo,
                     Materia materia) {
        this.nota = addNotaRequest.nota();
        this.notaAlunoId = new NotaAlunoId(aluno, periodo, materia);
        this.details = addNotaRequest.detailsNota();
    }
}

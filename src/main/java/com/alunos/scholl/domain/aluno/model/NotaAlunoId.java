package com.alunos.scholl.domain.aluno.model;

import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.periodo.model.Periodo;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class NotaAlunoId {
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "materia_id")
    Materia materia;

    public NotaAlunoId(Aluno aluno, Periodo periodo, Materia materia) {
        this.aluno = aluno;
        this.periodo = periodo;
        this.materia = materia;
    }

    public String toKeyString() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        var initial = this.materia.getNome();
        var endType = periodo.getTipo().toString();
        var dateInit = periodo.getInicio().format(formatador);
        var dateEnd = periodo.getFim().format(formatador);

        return String.format(
                "%s - %s de %s a %s",
                initial, endType, dateInit, dateEnd
        );
    }
}

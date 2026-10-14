package com.alunos.scholl.domain.materia.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.turma.model.Turma;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Materia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor")
    Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma")
    Turma turma;
}

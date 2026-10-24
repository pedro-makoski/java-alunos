package com.alunos.scholl.domain.materia.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.infra.standarts.HasDono;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia extends BaseEntity implements HasDono {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono")
    User dono;
}

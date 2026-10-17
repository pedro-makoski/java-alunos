package com.alunos.scholl.domain.aluno.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String nome;

    LocalDate dataNascimento;

    String nomePai;
    String nomeMae;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma")
    Turma turma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono")
    User dono;
}

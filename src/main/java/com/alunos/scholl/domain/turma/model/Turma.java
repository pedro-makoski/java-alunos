package com.alunos.scholl.domain.turma.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.infra.standarts.HasDono;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turma extends BaseEntity implements HasDono {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String nome;

    @Enumerated(EnumType.STRING)
    TipoTurma tipo;

    Long tamanhoMaximo;

    @ManyToOne
    @JoinColumn(name = "dono")
    User dono;
}

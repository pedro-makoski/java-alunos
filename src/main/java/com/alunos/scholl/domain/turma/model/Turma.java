package com.alunos.scholl.domain.turma.model;

import com.alunos.scholl.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Turma extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.STRING)
    TipoTurma tipo;

    Long tamanhoMaximo;
}

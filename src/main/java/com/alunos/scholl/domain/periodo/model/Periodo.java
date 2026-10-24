package com.alunos.scholl.domain.periodo.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.infra.standarts.HasDono;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Periodo extends BaseEntity implements HasDono {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.STRING)
    TipoPeriodo tipo;

    LocalDate inicio;
    LocalDate fim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono")
    User dono;
}

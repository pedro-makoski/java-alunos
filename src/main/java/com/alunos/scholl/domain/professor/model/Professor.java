package com.alunos.scholl.domain.professor.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono")
    User dono;

    @OneToOne
    @JoinColumn(name = "user_related")
    User userRelated;
}

package com.alunos.scholl.domain.turma.repositories;

import com.alunos.scholl.domain.turma.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TurmaRepository extends JpaRepository<Turma, UUID> {
}

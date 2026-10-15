package com.alunos.scholl.domain.turma.repositories;

import com.alunos.scholl.domain.turma.model.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TurmaRepository extends JpaRepository<Turma, UUID> {

    Page<Turma> findAllByDonoId(UUID id, Pageable pageable);
}

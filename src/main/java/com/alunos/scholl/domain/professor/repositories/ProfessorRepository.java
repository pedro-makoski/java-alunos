package com.alunos.scholl.domain.professor.repositories;

import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.turma.model.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    Page<Professor> findAllByDonoId(UUID id, Pageable pageable);
}

package com.alunos.scholl.domain.professor.repositories;

import com.alunos.scholl.domain.professor.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
}

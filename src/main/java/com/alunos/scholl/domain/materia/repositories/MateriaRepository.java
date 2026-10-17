package com.alunos.scholl.domain.materia.repositories;

import com.alunos.scholl.domain.materia.model.Materia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MateriaRepository extends JpaRepository<Materia, UUID> {
    Page<Materia> findByDonoId(UUID donoId, Pageable pageable);

    Page<Materia> findByProfessorId(UUID donoId, Pageable pageable);
}

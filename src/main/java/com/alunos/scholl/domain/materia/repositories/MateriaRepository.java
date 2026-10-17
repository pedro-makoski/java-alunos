package com.alunos.scholl.domain.materia.repositories;

import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.model.Materia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MateriaRepository extends JpaRepository<Materia, UUID> {
    Page<Materia> findByDonoId(UUID donoId, Pageable pageable);

    Page<Materia> findByProfessorId(UUID donoId, Pageable pageable);

    @Query("""
        SELECT m FROM Materia m
        JOIN m.turma t
        LEFT JOIN Aluno a
        
        WHERE a.id = :alunoId
        AND t = a.turma
    """)
    Page<Materia> findMateriasOfAluno(UUID alunoId, PageRequest of);
}

package com.alunos.scholl.domain.turma.repositories;

import com.alunos.scholl.domain.turma.model.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TurmaRepository extends JpaRepository<Turma, UUID> {

    Page<Turma> findAllByDonoId(UUID id, Pageable pageable);

    @Query("""
        SELECT DISTINCT t FROM Materia m
        LEFT JOIN m.turma
        WHERE m.professor.id = :id
    """)
    Page<Turma> findByTeacherId(UUID id, Pageable pageable);

    @Query("""
        SELECT DISTINCT t FROM Materia m
        LEFT JOIN m.turma t
        WHERE m.professor.id = :professorId
        AND t.id = :turmaId
    """)
    Optional<Turma> findByTurmaForTeacher(UUID turmaId, UUID professorId);
}

package com.alunos.scholl.domain.aluno.repositories;

import com.alunos.scholl.domain.aluno.model.Aluno;
import com.alunos.scholl.domain.professor.model.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Page<Aluno> findByDonoId(UUID dono, Pageable pageable);

    @Query("""
        SELECT DISTINCT a FROM Aluno a
        LEFT JOIN Materia m
        JOIN a.turma at
            
        WHERE m.professor.id = :id
        AND m.turma = at
    """)
    Page<Aluno> findByProfessorId(UUID id, Pageable pageable);

    @Query("""
        SELECT DISTINCT a FROM Aluno a
        LEFT JOIN Materia m
        JOIN a.turma at
            
        WHERE m.professor.id = :professorId
        AND m.turma = at
        AND a.id = :alunoId
    """)
    Optional<Aluno> findByAlunoForTeacher(UUID alunoId, UUID professorId);
}

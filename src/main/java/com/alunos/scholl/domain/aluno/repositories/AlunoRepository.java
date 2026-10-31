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
        JOIN a.turma at
        LEFT JOIN Materia m ON m.turma = at
            
        WHERE m.professor.id = :id
    """)
    Page<Aluno> findByProfessorId(UUID id, Pageable pageable);

    @Query("""
        SELECT DISTINCT a FROM Aluno a
        JOIN a.turma at
        LEFT JOIN Materia m ON m.turma = at
           
        WHERE m.professor.id = :professorId
        AND a.id = :alunoId
    """)
    Optional<Aluno> findByAlunoForTeacher(UUID alunoId, UUID professorId);
}

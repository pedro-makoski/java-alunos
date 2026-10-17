package com.alunos.scholl.domain.periodo.repositories;

import com.alunos.scholl.domain.periodo.model.Periodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PeriodoRepository extends JpaRepository<Periodo, UUID> {
    Page<Periodo> findByDonoId(UUID dono, Pageable pageable);

    @Query("""
        SELECT p FROM TurmaPeriodo tp
        JOIN tp.turmaPeriodoId tpi
        JOIN tpi.periodo p
        JOIN tpi.turma t
        LEFT JOIN Materia m
        JOIN m.professor prof
        JOIN m.turma turma
            
        WHERE prof.id = :profId
        AND m.turma = t
            
    """)
    Page<Periodo> findByProfessorId(UUID id, PageRequest of);
}

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
        LEFT JOIN Materia m on m.turma = t
        JOIN m.professor prof
        JOIN m.turma turma
            
        WHERE prof.id = :profId
            
    """)
    Page<Periodo> findByProfessorId(UUID id, PageRequest of);

    @Query("""
        SELECT p FROM TurmaPeriodo tp
        JOIN tp.turmaPeriodoId tpi
        JOIN tpi.periodo p
        JOIN tpi.turma t
        LEFT JOIN Materia m ON m.turma = t
        JOIN m.professor prof
        JOIN m.turma turma
            
        WHERE prof.id = :professorId
        AND p.id = :periodoId
    """)
    Optional<Periodo> findByPeriodoIdAndProfessorId(UUID periodoId, UUID professorId);

    @Query("""
        SELECT DISTINCT p FROM PeriodoMateria pm
        JOIN pm.periodoMateriaId.materia m
        JOIN pm.periodoMateriaId.periodo p
            
        WHERE m.id = :materiaId
    """)
    Page<Periodo> findByMateriaId(UUID materiaId, Pageable pageable);
}

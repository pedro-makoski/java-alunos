package com.alunos.scholl.domain.periodo.repositories;

import com.alunos.scholl.domain.periodo.model.Periodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PeriodoRepository extends JpaRepository<Periodo, UUID> {
    Page<Periodo> findByDonoId(UUID dono, Pageable pageable);
}

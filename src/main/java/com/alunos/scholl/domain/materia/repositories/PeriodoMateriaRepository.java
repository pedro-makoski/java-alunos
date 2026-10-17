package com.alunos.scholl.domain.materia.repositories;

import com.alunos.scholl.domain.materia.model.PeriodoMateria;
import com.alunos.scholl.domain.materia.model.PeriodoMateriaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodoMateriaRepository
        extends JpaRepository<PeriodoMateria, PeriodoMateriaId> {
}

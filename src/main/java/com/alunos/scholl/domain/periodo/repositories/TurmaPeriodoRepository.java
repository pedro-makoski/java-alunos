package com.alunos.scholl.domain.periodo.repositories;

import com.alunos.scholl.domain.periodo.model.TurmaPeriodo;
import com.alunos.scholl.domain.periodo.model.TurmaPeriodoId;
import com.alunos.scholl.domain.turma.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TurmaPeriodoRepository extends JpaRepository<TurmaPeriodo, TurmaPeriodoId> {

}

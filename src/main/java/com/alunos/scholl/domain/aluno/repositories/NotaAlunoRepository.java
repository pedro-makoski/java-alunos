package com.alunos.scholl.domain.aluno.repositories;

import com.alunos.scholl.domain.aluno.model.NotaAluno;
import com.alunos.scholl.domain.aluno.model.NotaAlunoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotaAlunoRepository extends JpaRepository<NotaAluno, NotaAlunoId> {
    List<NotaAluno> findByNotaAlunoIdAlunoId(UUID alunoId);

    List<NotaAluno> findByNotaAlunoIdMateriaIdAndNotaAlunoIdPeriodoId(UUID materiaId, UUID periodo);

    void deleteByNotaAlunoIdAlunoId(UUID alunoId);

    void deleteByNotaAlunoIdMateriaId(UUID materiaId);
}

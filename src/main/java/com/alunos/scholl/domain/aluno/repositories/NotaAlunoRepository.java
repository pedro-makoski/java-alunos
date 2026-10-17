package com.alunos.scholl.domain.aluno.repositories;

import com.alunos.scholl.domain.aluno.model.NotaAluno;
import com.alunos.scholl.domain.aluno.model.NotaAlunoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaAlunoRepository extends JpaRepository<NotaAluno, NotaAlunoId> {

}

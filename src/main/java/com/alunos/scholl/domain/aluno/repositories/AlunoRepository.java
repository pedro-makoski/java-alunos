package com.alunos.scholl.domain.aluno.repositories;

import com.alunos.scholl.domain.aluno.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Page<Aluno> findByDonoId(UUID dono, Pageable pageable);
}

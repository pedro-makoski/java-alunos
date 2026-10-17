CREATE INDEX id_user
ON users (id);

CREATE INDEX id_turma
ON turma (id);

CREATE INDEX dono_turma
    ON turma (dono);

CREATE INDEX id_aluno
ON aluno (id);

CREATE INDEX dono_aluno
ON aluno (dono);

CREATE INDEX periodo_id
ON periodo (id);

CREATE INDEX dono_periodo
ON periodo (dono);

CREATE INDEX turma_periodo_index
ON turma_periodo(turma_id, periodo_id);

CREATE INDEX professor_id
ON professor (id);

CREATE INDEX professor_dono
ON professor (dono);

CREATE INDEX materia_id
ON materia (id);

CREATE INDEX materia_dono
ON materia (dono);

CREATE INDEX periodo_materia_index
ON periodo_materia (periodo_id, materia_id);

CREATE INDEX nota_index
ON nota_aluno_periodo_materia (aluno_id, periodo_id, materia_id);

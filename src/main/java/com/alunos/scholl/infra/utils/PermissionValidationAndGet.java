package com.alunos.scholl.infra.utils;

import com.alunos.scholl.domain.aluno.exceptions.AlunoNotFound;
import com.alunos.scholl.domain.aluno.model.Aluno;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.materia.exception.MateriaNotFound;
import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.materia.repositories.MateriaRepository;
import com.alunos.scholl.domain.periodo.exceptions.PeriodoNotFound;
import com.alunos.scholl.domain.periodo.model.Periodo;
import com.alunos.scholl.domain.periodo.repositories.PeriodoRepository;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.turma.exceptions.TurmaNotFound;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.infra.exceptions.YouDontHavePermission;
import com.alunos.scholl.infra.standarts.HasDono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class PermissionValidationAndGet {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    public Aluno validateAndGetAluno(UUID alunoId, User user) {
        return switchFunctionOnTypeOfUser(
                user,
                () -> alunoRepository.findById(alunoId).orElseThrow(AlunoNotFound::new),
                professor ->
                    alunoRepository.findByAlunoForTeacher(alunoId, professor.getId()).
                            orElseThrow(AlunoNotFound::new)
        );
    }

    public Turma validateAndGetTurma(UUID turmaId, User user) {
        return switchFunctionOnTypeOfUser(
                user,
                () -> turmaRepository.findById(turmaId).orElseThrow(TurmaNotFound::new),
                professor ->
                    turmaRepository.findByTurmaForTeacher(turmaId, professor.getId())
                            .orElseThrow(TurmaNotFound::new)
        );
    }

    public Materia validateAndGetMateria(UUID materiaId, User user) {
        return switchFunctionOnTypeOfUser(
                user,
                () -> materiaRepository.findById(materiaId).orElseThrow(MateriaNotFound::new),
                professor ->
                        materiaRepository.findByIdAndProfessorId(materiaId, professor.getId())
                                .orElseThrow(MateriaNotFound::new)
        );
    }

    public Periodo validateAndGetPeriodo(UUID periodoId, User user) {
        return switchFunctionOnTypeOfUser(
                user,
                () -> periodoRepository.findById(periodoId).orElseThrow(PeriodoNotFound::new),
                professor ->
                        periodoRepository.findByPeriodoIdAndProfessorId(periodoId, professor.getId())
                                .orElseThrow(PeriodoNotFound::new)
        );
    }

    private <T extends HasDono> T switchFunctionOnTypeOfUser(
            User user,
            Supplier<T> getItem,
            Function<Professor, T> onTeacher) {
        if (user.isTeacher()) {
            return onTeacher.apply(user.getProfessor());
        }

        var item = getItem.get();

        if(!item.getDono().getId().equals(user.getId())) {
            throw new YouDontHavePermission();
        }

        return item;
    }
}

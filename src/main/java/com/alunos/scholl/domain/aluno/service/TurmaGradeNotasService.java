package com.alunos.scholl.domain.aluno.service;

import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.aluno.dto.response.GridNotas;
import com.alunos.scholl.domain.aluno.model.NotaAluno;
import com.alunos.scholl.domain.aluno.model.NotaAlunoId;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.aluno.repositories.NotaAlunoRepository;
import com.alunos.scholl.domain.materia.service.MateriaService;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.repositories.PeriodoRepository;
import com.alunos.scholl.domain.periodo.service.PeriodoService;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TurmaGradeNotasService {


    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private NotaAlunoRepository notaAlunoRepository;

    public GridNotas getNotasOfMateria(
            UUID materiaId,
            UUID periodoId,
            HttpServletRequest httpServletRequest
    ) {
        var user =  getUserOnRequest.realizeWithThrow(httpServletRequest);

        var finalUser = user.isTeacher() ?
                user.getProfessor().getDono() : user;

        var result = notaAlunoRepository.findByNotaAlunoIdMateriaAndPeriodo(materiaId, periodoId);

        return new GridNotas(
                result
                        .stream()
                        .map(GridNotas.UserItem::new)
                        .toList()
        );
    }

    public Page<PeriodoSample> getPeriodosOfMateria(
            HttpServletRequest httpServletRequest,
            UUID idMateria,
            int pageNumber
    ) {
        var user =  getUserOnRequest.realizeWithThrow(httpServletRequest);

        var finalUser = user.isTeacher() ?
                user.getProfessor().getDono() : user;

        return periodoRepository
                .findByMateriaId(idMateria, PageRequest.of(pageNumber, Constants.pageSize))
                .map(PeriodoSample::new);
    }
}

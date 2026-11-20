package com.alunos.scholl.domain.aluno.service;

import com.alunos.scholl.domain.aluno.dto.response.GridNotas;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.aluno.repositories.NotaAlunoRepository;
import com.alunos.scholl.domain.materia.service.MateriaService;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.repositories.PeriodoRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.Constants;
import com.alunos.scholl.infra.utils.PermissionValidationAndGet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private PermissionValidationAndGet permissionValidationAndGet;

    public GridNotas getNotasOfMateria(
            UUID materiaId,
            UUID periodoId,
            HttpServletRequest httpServletRequest
    ) {
        var user =  getUserOnRequest.realizeWithThrow(httpServletRequest);
        var periodo = permissionValidationAndGet.validateAndGetPeriodo(periodoId, user);

        var materia = permissionValidationAndGet.validateAndGetMateria(materiaId, user);

        var result = notaAlunoRepository.findByNotaAlunoIdMateriaIdAndNotaAlunoIdPeriodoId(materiaId, periodoId);

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

        var materia = permissionValidationAndGet.validateAndGetMateria(idMateria, user);

        return periodoRepository
                .findByMateriaId(materia.getId(), PageRequest.of(pageNumber, Constants.pageSize))
                .map(PeriodoSample::new);
    }
}

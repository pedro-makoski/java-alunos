package com.alunos.scholl.domain.aluno.service;

import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.aluno.dto.response.GetNotaAlunoResponse;
import com.alunos.scholl.domain.aluno.exceptions.AlunoNotaNotFound;
import com.alunos.scholl.domain.aluno.model.NotaAluno;
import com.alunos.scholl.domain.aluno.model.NotaAlunoId;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.aluno.repositories.NotaAlunoRepository;
import com.alunos.scholl.domain.materia.service.MateriaService;
import com.alunos.scholl.domain.periodo.service.PeriodoService;
import com.alunos.scholl.domain.turma.service.TurmaService;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class AlunoNotasService {

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private NotaAlunoRepository notaAlunoRepository;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private PeriodoService periodoService;

    public ResponseEntity<?> addNotaToAluno(
            @Valid AddNotaRequest addNotaRequest,
            HttpServletRequest httpServletRequest
            ) {
        var user =  getUserOnRequest.realizeWithThrow(httpServletRequest);

        var finalUser = user.isTeacher() ?
                user.getProfessor().getDono() : user;

        var materia = materiaService.getByMateriaId(addNotaRequest.materiaId(), finalUser);
        var aluno = alunoService.getAlunoById(addNotaRequest.idAluno(), finalUser);
        var periodo = periodoService.getPeriodoById(addNotaRequest.idPeriodo(), finalUser);

        var nota = notaAlunoRepository
                .findById(new NotaAlunoId(aluno, periodo, materia))
                .orElseGet(() -> new NotaAluno(
                        addNotaRequest,
                        aluno,
                        periodo,
                        materia
                ));

        nota.setNota(addNotaRequest.nota());
        nota.setDetails(addNotaRequest.detailsNota());

        return ResponseEntity
                .noContent()
                .build();
    }

    public GetNotaAlunoResponse getNotaAluno(
            HttpServletRequest httpServletRequest,
            UUID idAluno,
            UUID idMateria,
            UUID idPeriodo
    ) {
        var user =  getUserOnRequest.realizeWithThrow(httpServletRequest);

        var finalUser = user.isTeacher() ?
                user.getProfessor().getDono() : user;

        var materia = materiaService.getByMateriaId(idMateria, finalUser);
        var aluno = alunoService.getAlunoById(idAluno, finalUser);
        var periodo = periodoService.getPeriodoById(idPeriodo, finalUser);

        var nota = notaAlunoRepository.findById(new NotaAlunoId(
                aluno,
                periodo,
                materia
        )).orElseThrow(AlunoNotaNotFound::new);

        return new GetNotaAlunoResponse(nota);
    }
}

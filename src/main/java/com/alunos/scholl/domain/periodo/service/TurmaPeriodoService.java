package com.alunos.scholl.domain.periodo.service;

import com.alunos.scholl.domain.periodo.model.TurmaPeriodo;
import com.alunos.scholl.domain.periodo.repositories.TurmaPeriodoRepository;
import com.alunos.scholl.domain.turma.service.TurmaService;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TurmaPeriodoService {

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private PeriodoService periodoService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private TurmaPeriodoRepository turmaPeriodoRepository;

    public ResponseEntity<?> addTurmaInPeriodo(
            HttpServletRequest httpServletRequest,
            UUID turmaId,
            UUID periodoId
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var turma = turmaService.getTurma(turmaId, user);
        var periodo = periodoService.getPeriodoById(periodoId, user);

        var turmaPeriodo = new TurmaPeriodo(turma, periodo);

        turmaPeriodoRepository.save(turmaPeriodo);

        return ResponseEntity
                .noContent()
                .build();
    }
}

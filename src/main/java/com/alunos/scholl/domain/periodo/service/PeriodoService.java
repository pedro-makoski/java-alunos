package com.alunos.scholl.domain.periodo.service;

import com.alunos.scholl.domain.aluno.repositories.NotaAlunoRepository;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.request.PutPeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.CreatePeriodoResponse;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.exceptions.PeriodoNotFound;
import com.alunos.scholl.domain.periodo.model.Periodo;
import com.alunos.scholl.domain.periodo.repositories.PeriodoRepository;
import com.alunos.scholl.domain.periodo.repositories.TurmaPeriodoRepository;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.Constants;
import com.alunos.scholl.infra.utils.PermissionValidationAndGet;
import com.alunos.scholl.infra.utils.URIGeneratorOfBaseAndId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class PeriodoService {

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private PermissionValidationAndGet permissionValidationAndGet;

    @Autowired
    private NotaAlunoRepository notaAlunoRepository;

    @Autowired
    private TurmaPeriodoRepository turmaPeriodoRepository;

    public ResponseEntity<CreatePeriodoResponse>
        postPeriodo(@Valid CreatePeriodoRequest createPeriodoRequest,
                    HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var periodo = Periodo.builder()
                .tipo(createPeriodoRequest.tipoPeriodo())
                .inicio(createPeriodoRequest.inicio())
                .fim(createPeriodoRequest.fim())
                .dono(user)
                .build();

        var response = periodoRepository.save(periodo);

        return ResponseEntity
                .created(URIGeneratorOfBaseAndId.realize("/periodo/{id}", periodo.getId()))
                .body(new CreatePeriodoResponse(response.getId()));
    }

    public Page<PeriodoSample> getMyPeriodos(int pageNumber,
                                             HttpServletRequest httpServletRequest) {
            var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

            if(user.isTeacher()) {
                return periodoRepository.findByProfessorId(user.getProfessor().getId(),
                                PageRequest.of(pageNumber, Constants.pageSize))
                        .map(PeriodoSample::new);
            }

            return periodoRepository.findByDonoIdOrderByFimDesc(user.getId(),

                    PageRequest.of(pageNumber, Constants.pageSize))
                    .map(PeriodoSample::new);
    }

    public PeriodoSample getPeriodoById(UUID periodoId,
                                        HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        return new PeriodoSample(getPeriodoById(periodoId, user));
    }

    public Periodo getPeriodoById(UUID id, User user) {
        return permissionValidationAndGet.validateAndGetPeriodo(id, user);
    }

    public ResponseEntity<?> putPeriodo(
            UUID periodoId,
            @Valid PutPeriodoRequest putPeriodoRequest,
            HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var periodo = getPeriodoById(periodoId, user);

        periodo.setInicio(putPeriodoRequest.inicio());
        periodo.setFim(putPeriodoRequest.fim());
        periodo.setTipo(putPeriodoRequest.tipoPeriodo());

        periodoRepository.save(periodo);

        return ResponseEntity
                .noContent()
                .build();
    }

    public ResponseEntity<?> deletePeriodoById(UUID idPeriodo, HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var periodo = getPeriodoById(idPeriodo, user);

        turmaPeriodoRepository.deleteByTurmaPeriodoIdPeriodoId(idPeriodo);
        notaAlunoRepository.deleteByNotaAlunoIdPeriodoId(idPeriodo);
        periodoRepository.deleteById(periodo.getId());

        return ResponseEntity
                .noContent()
                .build();
    }
}

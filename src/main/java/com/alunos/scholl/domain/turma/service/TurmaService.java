package com.alunos.scholl.domain.turma.service;

import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.repositories.MateriaRepository;
import com.alunos.scholl.domain.turma.dto.request.CreateTurmaRequest;
import com.alunos.scholl.domain.turma.dto.request.PutTurmaRequest;
import com.alunos.scholl.domain.turma.dto.response.CreateTurmaResponse;
import com.alunos.scholl.domain.turma.dto.response.SampleTurma;
import com.alunos.scholl.domain.turma.exceptions.TurmaNotFound;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
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

import java.net.URI;
import java.util.UUID;

@Service
@Validated
public class TurmaService {

    @Autowired
    TurmaRepository turmaRepository;

    @Autowired
    private PermissionValidationAndGet permissionValidationAndGet;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private MateriaRepository materiaRepository;

    public ResponseEntity<CreateTurmaResponse> createTurma(
            @Valid CreateTurmaRequest createTurmaRequest,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var turma = Turma.builder()
                .nome(createTurmaRequest.nome())
                .tipo(createTurmaRequest.tipoTurma())
                .tamanhoMaximo(createTurmaRequest.tamanhoMaximo())
                .dono(user)
                .build();

        var res = turmaRepository.save(turma);

        return ResponseEntity
                .created(getUriOfTurma(turma))
                .body(new CreateTurmaResponse(
                        res.getId()
                ));
    }

    public Page<SampleTurma> getMyTurmas(
            HttpServletRequest httpServletRequest,
            String term,
            int pageNumber
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        if(user.isTeacher()) {
            return turmaRepository
                    .findByTeacherIdAndNome(user.getProfessor().getId(), term, PageRequest.of(pageNumber, Constants.pageSize))
                    .map(SampleTurma::new);
        }

        return turmaRepository
                .findAllByDonoIdAndNomeContaining(user.getId(), term,
                        PageRequest.of(pageNumber, Constants.pageSize))
                .map(SampleTurma::new);
    }

    public SampleTurma getTurmaById(UUID id, HttpServletRequest request) {
        var user = getUserOnRequest.realizeWithThrow(request);

        return new SampleTurma(getTurma(id, user));
    }

    public Turma getTurma(UUID idTurma, User user) {
        return permissionValidationAndGet.validateAndGetTurma(idTurma, user);
    }

    private URI getUriOfTurma(Turma turma) {
        return URIGeneratorOfBaseAndId
                .realize("/turma/{id}", turma.getId());
    }

    public ResponseEntity<?> putTurma(
            @Valid PutTurmaRequest putTurmaRequest,
            UUID idTurma,
            HttpServletRequest httpServletRequest
            ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var turma = getTurma(idTurma, user);

        turma.setNome(putTurmaRequest.nome());
        turma.setTipo(putTurmaRequest.tipoTurma());
        turma.setTamanhoMaximo(putTurmaRequest.tamanhoMaximo());

        turmaRepository.save(turma);

        return ResponseEntity
                .noContent()
                .build();
    }
}

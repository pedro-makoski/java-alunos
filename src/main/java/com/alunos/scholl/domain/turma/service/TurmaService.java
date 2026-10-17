package com.alunos.scholl.domain.turma.service;

import com.alunos.scholl.domain.turma.dto.request.CreateTurmaRequest;
import com.alunos.scholl.domain.turma.dto.response.CreateTurmaResponse;
import com.alunos.scholl.domain.turma.dto.response.SampleTurma;
import com.alunos.scholl.domain.turma.exceptions.TurmaNotFound;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.Constants;
import com.alunos.scholl.infra.exceptions.YouDontHavePermission;
import com.alunos.scholl.infra.utils.URIGeneratorOfBaseAndId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@Validated
public class TurmaService {

    @Autowired
    TurmaRepository turmaRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

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
            int pageNumber
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        return turmaRepository
                .findAllByDonoId(user.getId(), PageRequest.of(pageNumber, Constants.pageSize))
                .map(SampleTurma::new);
    }

    public SampleTurma getAlunoById(UUID id, HttpServletRequest request) {
        var user = getUserOnRequest.realizeWithThrow(request);

        return new SampleTurma(getTurma(id, user));
    }

    public Turma getTurma(UUID idTurma, User user) {
        var turma = turmaRepository.findById(idTurma)
                .orElseThrow(TurmaNotFound::new);

        if(!turma.getDono().getId().equals(user.getId())) {
            throw new YouDontHavePermission();
        }

        return turma;
    }

    private URI getUriOfTurma(Turma turma) {
        return URIGeneratorOfBaseAndId
                .realize("/turma/{id}", turma.getId());
    }
}

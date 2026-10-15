package com.alunos.scholl.domain.turma.service;

import com.alunos.scholl.domain.turma.dto.request.CreateTurmaRequest;
import com.alunos.scholl.domain.turma.dto.response.CreateTurmaResponse;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    private URI getUriOfTurma(Turma turma) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/turma/{id}")
                .buildAndExpand(turma.getId())
                .toUri();
    }
}

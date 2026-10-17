package com.alunos.scholl.domain.periodo.service;

import com.alunos.scholl.domain.aluno.dto.response.CreateAlunoResponse;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.CreatePeriodoResponse;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.model.Periodo;
import com.alunos.scholl.domain.periodo.repositories.PeriodoRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.Constants;
import com.alunos.scholl.infra.utils.URIGeneratorOfBaseAndId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class PeriodoService {

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private PeriodoRepository periodoRepository;

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

            return periodoRepository.findByDonoId(user.getId(),
                    PageRequest.of(pageNumber, Constants.pageSize))
                    .map(PeriodoSample::new);
    }
}

package com.alunos.scholl.domain.aluno.service;

import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.AlunoSample;
import com.alunos.scholl.domain.aluno.dto.response.CreateAlunoResponse;
import com.alunos.scholl.domain.aluno.exceptions.AlunoNotFound;
import com.alunos.scholl.domain.aluno.model.Aluno;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.turma.service.TurmaService;
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

import java.util.UUID;

@Service
@Validated
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private TurmaService turmaService;

    public ResponseEntity<CreateAlunoResponse> postAluno
            (@Valid CreateAlunoRequest createAlunoRequest,
             HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var aluno = Aluno.builder()
                .nome(createAlunoRequest.nome())
                .turma(turmaService.getTurma(createAlunoRequest.idTurma(), user))
                .dono(user)
                .nomePai(createAlunoRequest.nomePai())
                .nomeMae(createAlunoRequest.nomeMae())
                .dataNascimento(createAlunoRequest.dataNascimento())
                .build();

        var response = alunoRepository.save(aluno);

        return ResponseEntity
                .created(URIGeneratorOfBaseAndId.realize("/aluno/{id}",
                        response.getId()))
                .body(new CreateAlunoResponse(response.getId()));
    }

    public Page<AlunoSample> getAlunos(int pageNumber, HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        if(user.isTeacher()) {
            return alunoRepository
                    .findByProfessorId(user.getId(), PageRequest.of(pageNumber, Constants.pageSize))
                    .map(AlunoSample::new);
        }

        return alunoRepository
                    .findByDonoId(user.getId(), PageRequest.of(pageNumber, Constants.pageSize))
                    .map(AlunoSample::new);
    }

    public AlunoSample getAlunoById(UUID id, HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        return new AlunoSample(getAlunoById(id, user));
    }

    public ResponseEntity<?> updateAlunoTurma(
            UUID alunoId,
            UUID turmaId,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var aluno = getAlunoById(alunoId, user);
        var turma = turmaService.getTurma(turmaId, user);

        aluno.setTurma(turma);

        alunoRepository.save(aluno);

        return ResponseEntity
                .noContent()
                .build();
    }

    public Aluno getAlunoById(UUID id, User user) {
        var aluno = alunoRepository.findById(id)
                .orElseThrow(AlunoNotFound::new);

        if(!aluno.getDono().getId().equals(user.getId())) {
            throw new YouDontHavePermission();
        }

        return aluno;
    }
}

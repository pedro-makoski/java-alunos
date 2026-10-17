package com.alunos.scholl.domain.aluno.service;

import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.CreateAlunoResponse;
import com.alunos.scholl.domain.aluno.model.Aluno;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.turma.service.TurmaService;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.utils.URIGeneratorOfBaseAndId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private TurmaService turmaService;

    public ResponseEntity<CreateAlunoResponse> postUAluno
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
}

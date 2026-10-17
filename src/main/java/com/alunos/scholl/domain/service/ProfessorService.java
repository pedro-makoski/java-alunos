package com.alunos.scholl.domain.service;

import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.response.CreateProfessorResponse;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.professor.repositories.ProfessorRepository;
import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.domain.user.service.LoginService;
import com.alunos.scholl.infra.utils.URIGeneratorOfBaseAndId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProfessorService {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    public ResponseEntity<CreateProfessorResponse> createProfessor(
            @Valid CreateProfessorRequest createProfessorRequest,
            HttpServletRequest httpServletRequest
            ) {
        var dono = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var newUser = loginService.createUser(new CreateAccountRequest(
            createProfessorRequest.email(), createProfessorRequest.senha(),
            createProfessorRequest.dataNascimento(), createProfessorRequest.nome(),
            createProfessorRequest.sobrenome()
        ));

        var professor = Professor.builder()
                .userRelated(newUser)
                .dono(dono)
                .build();

        var professorResponse = professorRepository.save(professor);

        newUser.setProfessor(professorResponse);

        return ResponseEntity
                .created(URIGeneratorOfBaseAndId.realize("/professor/{id}",
                        professorResponse.getId()))
                .body(new CreateProfessorResponse(professorResponse.getId()));
    }
}

package com.alunos.scholl.domain.professor.service;

import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.response.CreateProfessorResponse;
import com.alunos.scholl.domain.professor.exceptions.ProfessorNotFoundException;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.professor.repositories.ProfessorRepository;
import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import com.alunos.scholl.domain.professor.dto.response.SampleProfessor;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.domain.user.service.LoginService;
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

        userRepository.save(newUser);

        return ResponseEntity
                .created(URIGeneratorOfBaseAndId.realize("/professor/{id}",
                        professorResponse.getId()))
                .body(new CreateProfessorResponse(professorResponse.getId()));
    }

    public Page<SampleProfessor> findMyProfessor(
            int pageNumber,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        return professorRepository
                .findAllByDonoId(user.getId(), PageRequest.of(pageNumber, Constants.pageSize))
                .map(SampleProfessor::new);
    }

    public SampleProfessor getProfessorById(
            UUID professorId,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        return new SampleProfessor(getProfessorById(professorId, user));
    }

    public Professor getProfessorById(
            UUID professorId,
            User user
    ) {
        var professor = professorRepository.findById(professorId)
                .orElseThrow(ProfessorNotFoundException::new);

        if(!professor.getDono().getId().equals(user.getId())) {
            throw new YouDontHavePermission();
        }

        return professor;
    }
}

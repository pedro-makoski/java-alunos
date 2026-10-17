package com.alunos.scholl.domain.materia.service;

import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.CreateMateriaResponse;
import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.materia.repositories.MateriaRepository;
import com.alunos.scholl.domain.service.ProfessorService;
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
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private TurmaService turmaService;

    public ResponseEntity<CreateMateriaResponse> postMateria(
            @Valid CreateMateriaRequest createMateriaRequest,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var turma = turmaService.getTurma(createMateriaRequest.turmaId(),
                user);
        var professor = professorService
                .getProfessorById(createMateriaRequest.professorId(), user);

        var materia = Materia.builder()
                .nome(createMateriaRequest.nome())
                .dono(user)
                .professor(professor)
                .turma(turma)
                .build();

        var result = materiaRepository.save(materia);

        return ResponseEntity
                .created(URIGeneratorOfBaseAndId.realize
                            ("/materia/{id}", result.getId()))
                .body(new CreateMateriaResponse(result.getId()));
    }
}

package com.alunos.scholl.domain.materia.service;

import com.alunos.scholl.domain.aluno.repositories.NotaAlunoRepository;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.request.PutMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.CreateMateriaResponse;
import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.exception.MateriaNotFound;
import com.alunos.scholl.domain.materia.model.Materia;
import com.alunos.scholl.domain.materia.repositories.MateriaRepository;
import com.alunos.scholl.domain.professor.service.ProfessorService;
import com.alunos.scholl.domain.turma.service.TurmaService;
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
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private PermissionValidationAndGet permissionValidationAndGet;

    @Autowired
    private NotaAlunoRepository notaAlunoRepository;

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

    public ResponseEntity<?> changeTurmaInMateria(
            UUID idTurma,
            UUID idMateria,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var materia = getByMateriaId(idMateria, user);
        var turma = turmaService.getTurma(idTurma, user);

        materia.setTurma(turma);

        materiaRepository.save(materia);

        return ResponseEntity
                .noContent()
                .build();
    }

    public Page<MateriaSample> findMyMaterias(
            int pageNumber,
            String term,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        if(user.isTeacher()) {
            materiaRepository
                    .findByProfessorIdAndNomeContaining(
                            user.getProfessor().getId(),
                            term,
                            PageRequest.of(pageNumber, Constants.pageSize))
                    .map(MateriaSample::new);
        }

        return materiaRepository
                .findByDonoIdAndNomeContaining(user.getId(), term,
                        PageRequest.of(pageNumber, Constants.pageSize))
                .map(MateriaSample::new);
    }

    public MateriaSample getByMateriaId(
            UUID materiaId,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        return new MateriaSample(getByMateriaId(materiaId, user));
    }

    public ResponseEntity<?> putMateriaById(
            @Valid PutMateriaRequest putMateriaRequest,
            UUID idMateria,
            HttpServletRequest httpServletRequest
            ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var materia = getByMateriaId(idMateria, user);

        var turma = turmaService.getTurma(putMateriaRequest.turmaId(),
                user);
        var professor = professorService
                .getProfessorById(putMateriaRequest.professorId(), user);

        materia.setNome(putMateriaRequest.nome());
        materia.setTurma(turma);
        materia.setProfessor(professor);

        materiaRepository.save(materia);

        return ResponseEntity
                .noContent()
                .build();
    }

    public ResponseEntity<?> deleteMateriaById(
            UUID idMateria,
            HttpServletRequest httpServletRequest) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var materia = getByMateriaId(idMateria, user);

        notaAlunoRepository.deleteByNotaAlunoIdMateriaId(materia.getId());
        materiaRepository.deleteById(idMateria);

        return ResponseEntity
                .noContent()
                .build();
    }

    public Materia getByMateriaId(
            UUID materiaId,
            User user
    ) {
        return permissionValidationAndGet.validateAndGetMateria(materiaId, user);
    }
}

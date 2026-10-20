package com.alunos.scholl.domain.materia;

import com.alunos.scholl.domain.aluno.dto.response.GridNotas;
import com.alunos.scholl.domain.aluno.service.TurmaGradeNotasService;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.CreateMateriaResponse;
import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.service.FindMyAlunosService;
import com.alunos.scholl.domain.materia.service.MateriaService;
import com.alunos.scholl.domain.materia.service.PeriodoMateriaService;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private TurmaGradeNotasService turmaGradeNotasService;

    @Autowired
    private FindMyAlunosService findMyAlunosService;

    @Autowired
    private PeriodoMateriaService periodoMateriaService;

    @PostMapping("/create")
    public ResponseEntity<CreateMateriaResponse> create(
            @RequestBody CreateMateriaRequest createMateriaRequest,
            HttpServletRequest httpServletRequest
    ) {
        return materiaService.postMateria(createMateriaRequest, httpServletRequest);
    }

    @GetMapping
    public Page<MateriaSample> getMyMaterias(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return materiaService.findMyMaterias(pageNumber, httpServletRequest);
    }

    @GetMapping("/{id}")
    public MateriaSample getMateriaById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return materiaService.getByMateriaId(id, request);
    }

    @GetMapping("/change-turma-materia/{materiaId}")
    public ResponseEntity<?> changeTurmaInMateria(
            @RequestParam UUID turmaId,
            @PathVariable UUID materiaId,
            HttpServletRequest httpServletRequest
    ) {
        return materiaService.changeTurmaInMateria(turmaId, materiaId, httpServletRequest);
    }

    @GetMapping("/grid-materias/get")
    public GridNotas getNotasOfMateria(
            @RequestParam UUID periodoId,
            @RequestParam UUID materiaId,
            HttpServletRequest httpServletRequest
    ) {
        return turmaGradeNotasService
                .getNotasOfMateria(materiaId, periodoId, httpServletRequest);
    }

    @GetMapping("/grid-materia/get-periodos")
    public Page<PeriodoSample> getPeriodosOfMateria(
            HttpServletRequest httpServletRequest,
            @RequestParam UUID idMateria,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return turmaGradeNotasService
                .getPeriodosOfMateria(httpServletRequest, idMateria, pageNumber);
    }

    @GetMapping("/materias-of-aluno/{alunoId}")
    public Page<MateriaSample> getMateriasOfAluno(
            HttpServletRequest httpServletRequest,
            @PathVariable UUID alunoId,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return findMyAlunosService.getMateriasOfAluno(httpServletRequest, alunoId, pageNumber);
    }


    @PatchMapping("/add-periodo-in-materia/{materiaId}")
    public ResponseEntity<?> addPeriodoInMateria(
            HttpServletRequest httpServletRequest,
            @RequestParam UUID periodoId,
            @PathVariable UUID materiaId
    ) {
        return periodoMateriaService
                .addPeriodoInMateria(httpServletRequest, periodoId, materiaId);
    }
}

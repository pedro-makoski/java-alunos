package com.alunos.scholl.domain.aluno;

import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.*;
import com.alunos.scholl.domain.aluno.service.AlunoNotasService;
import com.alunos.scholl.domain.aluno.service.AlunoOverviewService;
import com.alunos.scholl.domain.aluno.service.AlunoService;
import com.alunos.scholl.domain.aluno.service.TurmaGradeNotasService;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private AlunoOverviewService alunoOverviewService;

    @Autowired
    private AlunoNotasService alunoNotasService;

    @PostMapping("/create")
    public ResponseEntity<CreateAlunoResponse> create(
            @RequestBody CreateAlunoRequest createAlunoRequest,
            HttpServletRequest httpServletRequest
    ) {
        return alunoService.postAluno(createAlunoRequest, httpServletRequest);
    }

    @GetMapping
    public Page<AlunoSample> getMyAlunos(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return alunoService.getAlunos(pageNumber, httpServletRequest);
    }

    @GetMapping("/{id}")
    public AlunoSample getAlunoById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return alunoService.getAlunoById(id, request);
    }


    @PatchMapping("/turma/change")
    public ResponseEntity<?> updateAlunoTurma(
            @RequestParam UUID alunoId,
            @RequestParam UUID turmaId,
            HttpServletRequest httpServletRequest
    ) {
        return alunoService.updateAlunoTurma(alunoId, turmaId, httpServletRequest);
    }

    @GetMapping("/overview/{id}")
    public AlunoDetails getAlunoDetails(
            @PathVariable UUID idAluno,
            HttpServletRequest httpServletRequest
    ) {
        return alunoOverviewService.getAlunoDetails(idAluno, httpServletRequest);
    }

    @PostMapping("/nota-aluno/create")
    public ResponseEntity<?> addNotaToAluno(
            @RequestBody AddNotaRequest addNotaRequest,
            HttpServletRequest httpServletRequest
    ) {
        return alunoNotasService.addNotaToAluno(addNotaRequest, httpServletRequest);
    }

    @GetMapping("/nota-aluno/post")
    public GetNotaAlunoResponse getNotaAluno(
            HttpServletRequest httpServletRequest,
            @RequestParam UUID idAluno,
            @RequestParam UUID idMateria,
            @RequestParam UUID idPeriodo
    ) {
        return alunoNotasService.getNotaAluno(httpServletRequest, idAluno, idMateria, idPeriodo);
    }
}

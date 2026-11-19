package com.alunos.scholl.domain.aluno;

import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.request.PutAlunoRequest;
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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public ResponseEntity<CreateAlunoResponse> create(
            @RequestBody CreateAlunoRequest createAlunoRequest,
            HttpServletRequest httpServletRequest
    ) {
        return alunoService.postAluno(createAlunoRequest, httpServletRequest);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<AlunoSample> getMyAlunos(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "", required = false) String searchTerm
    ) {
        return alunoService.getAlunos(pageNumber, searchTerm, httpServletRequest);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public AlunoSample getAlunoById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return alunoService.getAlunoById(id, request);
    }


    @PatchMapping("/turma/change")
    @Transactional
    public ResponseEntity<?> updateAlunoTurma(
            @RequestParam UUID alunoId,
            @RequestParam UUID turmaId,
            HttpServletRequest httpServletRequest
    ) {
        return alunoService.updateAlunoTurma(alunoId, turmaId, httpServletRequest);
    }

    @GetMapping("/overview/{id}")
    @Transactional(readOnly = true)
    public AlunoDetails getAlunoDetails(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest
    ) {
        return alunoOverviewService.getAlunoDetails(id, httpServletRequest);
    }

    @PostMapping("/nota-aluno/create")
    @Transactional
    public ResponseEntity<?> addNotaToAluno(
            @RequestBody AddNotaRequest addNotaRequest,
            HttpServletRequest httpServletRequest
    ) {
        return alunoNotasService.addNotaToAluno(addNotaRequest, httpServletRequest);
    }

    @GetMapping("/nota-aluno/details")
    @Transactional
    public GetNotaAlunoResponse getNotaAluno(
            @RequestParam UUID idAluno,
            @RequestParam UUID idMateria,
            @RequestParam UUID idPeriodo,
            HttpServletRequest httpServletRequest
    ) {
        return alunoNotasService.getNotaAluno(httpServletRequest, idAluno, idMateria, idPeriodo);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> putAluno(
            @PathVariable UUID id,
            @RequestBody PutAlunoRequest putAlunoRequest,
            HttpServletRequest httpServletRequest
            ) {
        return alunoService.putAluno(putAlunoRequest, id, httpServletRequest);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteAluno(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest
    ) {
        return alunoService.deleteAluno(id, httpServletRequest);
    }
}

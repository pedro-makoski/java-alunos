package com.alunos.scholl.domain.periodo;

import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.request.PutPeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.CreatePeriodoResponse;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.service.PeriodoService;
import com.alunos.scholl.domain.periodo.service.TurmaPeriodoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/periodo")
public class PeriodoController {

    @Autowired
    private PeriodoService periodoService;

    @Autowired
    private TurmaPeriodoService turmaPeriodoService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<CreatePeriodoResponse> create(
            @RequestBody CreatePeriodoRequest createPeriodoRequest,
            HttpServletRequest httpServletRequest
    ) {
        return periodoService.postPeriodo(createPeriodoRequest, httpServletRequest);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<PeriodoSample> getMyPeriodos(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return periodoService.getMyPeriodos(pageNumber, httpServletRequest);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PeriodoSample getPeriodoById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return periodoService.getPeriodoById(id, request);
    }

    @PatchMapping("/add-turma-in-periodo/{periodoId}")
    @Transactional
    public ResponseEntity<?> addTurmaInPeriodo(
            HttpServletRequest httpServletRequest,
            @RequestParam UUID turmaId,
            @PathVariable UUID periodoId
    ) {
        return turmaPeriodoService.addTurmaInPeriodo(httpServletRequest, turmaId, periodoId);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletePeriodo(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest
    ) {
        return periodoService.deletePeriodoById(id, httpServletRequest);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> putPeriodo(
            @RequestBody PutPeriodoRequest putPeriodoRequest,
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest
            ) {
           return periodoService.putPeriodo(id, putPeriodoRequest, httpServletRequest);
    }
}

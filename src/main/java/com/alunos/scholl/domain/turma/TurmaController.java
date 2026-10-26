package com.alunos.scholl.domain.turma;

import com.alunos.scholl.domain.turma.dto.request.CreateTurmaRequest;
import com.alunos.scholl.domain.turma.dto.response.CreateTurmaResponse;
import com.alunos.scholl.domain.turma.dto.response.SampleTurma;
import com.alunos.scholl.domain.turma.service.TurmaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/turma")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<CreateTurmaResponse> createTurma(
            @RequestBody CreateTurmaRequest createTurmaRequest,
            HttpServletRequest httpServletRequest
    ) {
        return turmaService.createTurma(createTurmaRequest, httpServletRequest);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<SampleTurma> getMyTurmas(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return turmaService.getMyTurmas(httpServletRequest, pageNumber);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public SampleTurma getTurmaById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return turmaService.getTurmaById(id, request);
    }
}

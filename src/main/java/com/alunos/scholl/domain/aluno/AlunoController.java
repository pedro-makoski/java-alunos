package com.alunos.scholl.domain.aluno;

import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.AlunoSample;
import com.alunos.scholl.domain.aluno.dto.response.CreateAlunoResponse;
import com.alunos.scholl.domain.aluno.service.AlunoService;
import jakarta.servlet.http.HttpServletRequest;
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
}

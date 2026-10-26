package com.alunos.scholl.domain.professor;

import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.response.CreateProfessorResponse;
import com.alunos.scholl.domain.professor.dto.response.SampleProfessor;
import com.alunos.scholl.domain.professor.service.ProfessorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<CreateProfessorResponse> create(
            @RequestBody CreateProfessorRequest createProfessorRequest,
            HttpServletRequest httpServletRequest
    ) {
        return professorService.createProfessor(createProfessorRequest, httpServletRequest);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<SampleProfessor> getMyProfessores(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return professorService.findMyProfessor(pageNumber, httpServletRequest);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public SampleProfessor getProfessorById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return professorService.getProfessorById(id, request);
    }
}

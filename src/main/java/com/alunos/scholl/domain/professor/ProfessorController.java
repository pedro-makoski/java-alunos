package com.alunos.scholl.domain.professor;

import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.request.PutProfessorRequest;
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
            @RequestParam(defaultValue = "", required = false) String term,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return professorService.findMyProfessor(pageNumber, term, httpServletRequest);
    }

    @GetMapping("/{id}")
    @Transactional
    public SampleProfessor getProfessor(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest
    ) {
        return professorService.getProfessorById(id, httpServletRequest);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProfessor(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest
    ) {
        return professorService.deleteProfessor(id, httpServletRequest);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> putProfessor(
            @PathVariable UUID id,
            @RequestBody PutProfessorRequest putProfessorRequest,
            HttpServletRequest httpServletRequest
    ) {
        return professorService.putProfessor(putProfessorRequest, id, httpServletRequest);
    }

}

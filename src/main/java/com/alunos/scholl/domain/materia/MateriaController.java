package com.alunos.scholl.domain.materia;

import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.CreateMateriaResponse;
import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.service.MateriaService;
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
}

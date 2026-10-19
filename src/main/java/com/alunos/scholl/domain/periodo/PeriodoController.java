package com.alunos.scholl.domain.periodo;

import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.CreatePeriodoResponse;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.service.PeriodoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/periodo")
public class PeriodoController {

    @Autowired
    private PeriodoService periodoService;

    @PostMapping("/create")
    public ResponseEntity<CreatePeriodoResponse> create(
            @RequestBody CreatePeriodoRequest createPeriodoRequest,
            HttpServletRequest httpServletRequest
    ) {
        return periodoService.postPeriodo(createPeriodoRequest, httpServletRequest);
    }

    @GetMapping
    public Page<PeriodoSample> getMyPeriodos(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0", required = false) int pageNumber
    ) {
        return periodoService.getMyPeriodos(pageNumber, httpServletRequest);
    }

    @GetMapping("/{id}")
    public PeriodoSample getPeriodoById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        return periodoService.getPeriodoById(id, request);
    }
}

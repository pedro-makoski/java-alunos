package com.alunos.scholl.domain.materia.service;

import com.alunos.scholl.domain.materia.model.PeriodoMateria;
import com.alunos.scholl.domain.materia.model.PeriodoMateriaId;
import com.alunos.scholl.domain.materia.repositories.PeriodoMateriaRepository;
import com.alunos.scholl.domain.periodo.service.PeriodoService;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PeriodoMateriaService {

    @Autowired
    private PeriodoMateriaRepository periodoMateriaRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private PeriodoService periodoService;

    public ResponseEntity<?> addPeriodoInMateria(
            HttpServletRequest httpServletRequest,
            UUID idPeriodo,
            UUID idMateria
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var materia = materiaService.getByMateriaId(idMateria, user);
        var periodo = periodoService.getPeriodoById(idPeriodo, user);

        var periodoMateria = new PeriodoMateria(materia, periodo);

        periodoMateriaRepository.save(periodoMateria);

        return ResponseEntity
                .noContent()
                .build();
    }
}

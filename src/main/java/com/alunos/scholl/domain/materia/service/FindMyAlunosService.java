package com.alunos.scholl.domain.materia.service;

import com.alunos.scholl.domain.aluno.service.AlunoService;
import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.repositories.MateriaRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.Constants;
import com.alunos.scholl.infra.exceptions.YouDontHavePermission;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindMyAlunosService {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private GetUserOnRequest getUserOnRequest;

    public Page<MateriaSample> getMateriasOfAluno(
            HttpServletRequest httpServletRequest,
            UUID alunoId,
            int pageNumber
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);
        var aluno = alunoService.getAlunoById(alunoId, user);

        if(!aluno.getDono().getId().equals(user.getId())) {
            throw new YouDontHavePermission();
        }

        return materiaRepository.findMateriasOfAluno(alunoId,
                    PageRequest.of(pageNumber, Constants.pageSize))
                .map(MateriaSample::new);
    }
}

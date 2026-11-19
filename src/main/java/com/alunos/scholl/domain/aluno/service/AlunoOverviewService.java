package com.alunos.scholl.domain.aluno.service;

import com.alunos.scholl.domain.aluno.dto.response.AlunoDetails;
import com.alunos.scholl.domain.aluno.exceptions.NoAlunoFound;
import com.alunos.scholl.domain.aluno.repositories.NotaAlunoRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.infra.utils.PermissionValidationAndGet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoOverviewService {

    @Autowired
    GetUserOnRequest getUserOnRequest;

    @Autowired
    AlunoService alunoService;

    @Autowired
    NotaAlunoRepository notaAlunoRepository;

    @Autowired
    PermissionValidationAndGet permissionValidationAndGet;

    public AlunoDetails getAlunoDetails(
            UUID idAluno,
            HttpServletRequest httpServletRequest
    ) {
        var user = getUserOnRequest.realizeWithThrow(httpServletRequest);

        var finalUser = user.isTeacher() ?
                user.getProfessor().getDono() : user;

        var aluno = permissionValidationAndGet.validateAndGetAluno(idAluno, user);

        var notas = notaAlunoRepository.findByNotaAlunoIdAlunoId(idAluno);

        if(notas.isEmpty()) {
            throw new NoAlunoFound();
        }

        Double media = notas
                .stream()
                .map(n -> n.getNota())
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        Map<String, Double> notasRelation = notas
                .stream()
                .collect(Collectors.toMap(
                        n -> n.getNotaAlunoId().toKeyString(),
                        n -> n.getNota()
                ));

        return new AlunoDetails(
                aluno.getId(),
                media,
                notasRelation,
                aluno.getNome()
        );
    }
}

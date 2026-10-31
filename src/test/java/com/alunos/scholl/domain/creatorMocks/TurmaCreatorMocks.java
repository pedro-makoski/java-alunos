package com.alunos.scholl.domain.creatorMocks;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.turma.model.TipoTurma;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
import com.alunos.scholl.domain.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.stereotype.Component;

@Component
@TestConfiguration
public class TurmaCreatorMocks {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private TestUtils testUtils;

    public Turma createTurmaRandom(User user) {
        return turmaRepository.save(Turma.builder()
                .dono(user)
                .tipo(TipoTurma.FUNDAMENTAL)
                .tamanhoMaximo(500L)
                .nome(testUtils.getRandomNome())
                .build());
    }
}

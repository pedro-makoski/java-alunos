package com.alunos.scholl.domain.creatorMocks;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.CreatePeriodoResponse;
import com.alunos.scholl.domain.periodo.model.TipoPeriodo;
import com.alunos.scholl.domain.turma.dto.request.CreateTurmaRequest;
import com.alunos.scholl.domain.turma.dto.response.CreateTurmaResponse;
import com.alunos.scholl.domain.turma.model.TipoTurma;
import com.alunos.scholl.domain.turma.model.Turma;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
import com.alunos.scholl.domain.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@TestConfiguration
public class TurmaCreatorMocks {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreateTurmaRequest> createTurmaRequestJacksonTester;

    @Autowired
    private JacksonTester<CreateTurmaResponse> createTurmaResponseJacksonTester;

    public Turma createTurmaRandom(User user) {
        return turmaRepository.save(Turma.builder()
                .dono(user)
                .tipo(TipoTurma.FUNDAMENTAL)
                .tamanhoMaximo(500L)
                .nome(testUtils.getRandomNome())
                .build());
    }

    public CreateTurmaResponse createRandomTurma() throws Exception {
        return createTurmaRequest(
                new CreateTurmaRequest(
                        testUtils.getRandomNome(),
                        TipoTurma.MEDIO,
                        20L
                )
        );
    }

    public CreateTurmaResponse createTurmaRequest(CreateTurmaRequest
                                                              createTurmaRequest)
            throws Exception{
        var result = createTurma(createTurmaRequest);

        return createTurmaResponseJacksonTester.parseObject(result.getContentAsString());
    }

    public MockHttpServletResponse createTurma(CreateTurmaRequest createTurmaRequest) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post("/turma/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    createTurmaRequestJacksonTester
                                            .write(createTurmaRequest).getJson()))
                    .andReturn().getResponse();
        } catch (Exception e) {
            fail("Erro ao enviar req: " + e.getMessage());
            return null;
        }
    }
}

package com.alunos.scholl.domain.creatorMocks;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.CreateAlunoResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@TestConfiguration
public class AlunoCreatorMocks {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private JacksonTester<CreateAlunoRequest> createAlunoRequest;

    @Autowired
    private JacksonTester<CreateAlunoResponse> createAlunoResponseJacksonTester;

    @Autowired
    private TurmaCreatorMocks turmaCreatorMocks;

    public CreateAlunoResponse createAlunoRandom(User user) throws Exception {
        var turma = turmaCreatorMocks.createTurmaRandom(user);

        return createAlunoRandom(user, turma.getId());
    }

    public List<CreateAlunoResponse> createMultiplesAlunos(User user, int quant) throws Exception {
        var turma = turmaCreatorMocks.createTurmaRandom(user);

        return createMultiplesAlunos(user, quant, turma.getId());
    }

    public List<CreateAlunoResponse> createMultiplesAlunos(User user,
                                                           int quant, UUID idTurma)
            throws Exception {

        var responses = new ArrayList<CreateAlunoResponse>();

        for(var i = 0; i < quant; i++) {
            var result = createAlunoRandom(user, idTurma);

            responses.add(result);
        }

        return responses;
    }


    public CreateAlunoResponse createAlunoRandom(User user, UUID turmaId) throws Exception {

        var aluno = new CreateAlunoRequest(
                turmaId,
                testUtils.getRandomNome(),
                testUtils.getRandomDate(),
                testUtils.getNome(),
                testUtils.getRandomNome()
        );

        var alunoResponse = sendAlunoRequest(aluno);

        return createAlunoResponseJacksonTester.parseObject(alunoResponse.getContentAsString());
    }

    public CreateAlunoResponse createAluno(CreateAlunoRequest createAlunoRequest)
            throws Exception {
        return createAlunoResponseJacksonTester.parseObject(sendAlunoRequest(createAlunoRequest).getContentAsString());
    }

    public MockHttpServletResponse sendAlunoRequest(CreateAlunoRequest createAlunoRequestVar) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post("/aluno/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createAlunoRequest.write(createAlunoRequestVar).getJson()))
                    .andReturn().getResponse();
        } catch (Exception e) {
            fail("Erro ao enviar req: " + e.getMessage());
            return null;
        }
    }
}

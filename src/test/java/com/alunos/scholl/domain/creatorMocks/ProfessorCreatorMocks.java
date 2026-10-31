package com.alunos.scholl.domain.creatorMocks;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.response.CreateProfessorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@TestConfiguration
public class ProfessorCreatorMocks {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreateProfessorRequest> createProfessorRequestJacksonTester;

    @Autowired
    private JacksonTester<CreateProfessorResponse> createProfessorResponseJacksonTester;

    @Autowired
    private TestUtils testUtils;

    public CreateProfessorResponse createRandomProfessor() throws Exception {
        return createProfessorRequest(
                new CreateProfessorRequest(
                        testUtils.getRandomNome()+"@email.com",
                        testUtils.getRandomNome(),
                        testUtils.getRandomNome(),
                        testUtils.getRandomNome(),
                        testUtils.getRandomDate()
                )
        );
    }

    public CreateProfessorResponse createProfessorRequest(CreateProfessorRequest
                                                                  createProfessorRequest)
            throws Exception{
        var result = createProfessor(createProfessorRequest);

        return createProfessorResponseJacksonTester.parseObject(result.getContentAsString());
    }

    public MockHttpServletResponse createProfessor(CreateProfessorRequest createProfessorRequest) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post("/professor/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    createProfessorRequestJacksonTester
                                            .write(createProfessorRequest).getJson()))
                    .andReturn().getResponse();
        } catch (Exception e) {
            fail("Erro ao enviar req: " + e.getMessage());
            return null;
        }
    }
}

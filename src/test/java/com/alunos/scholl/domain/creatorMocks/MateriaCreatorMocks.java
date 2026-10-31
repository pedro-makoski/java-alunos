package com.alunos.scholl.domain.creatorMocks;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.CreateMateriaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@TestConfiguration

public class MateriaCreatorMocks {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreateMateriaRequest> createMateriaRequestJacksonTester;

    @Autowired
    private JacksonTester<CreateMateriaResponse> createMateriaResponseJacksonTester;

    @Autowired
    private TestUtils testUtils;


    public CreateMateriaResponse createRandomMateria(UUID turmaId, UUID professorId) throws Exception {
        return createMateriaConverted(new CreateMateriaRequest(
                testUtils.getRandomNome(),
                professorId,
                turmaId
        ));
    }

    public CreateMateriaResponse createMateriaConverted
            (CreateMateriaRequest createMateriaRequest) throws Exception {
        var result = createMateria(createMateriaRequest);

        return createMateriaResponseJacksonTester.parseObject(result.getContentAsString());
    }

    public MockHttpServletResponse createMateria(CreateMateriaRequest createMateriaRequest) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post("/materia/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    createMateriaRequestJacksonTester
                                            .write(createMateriaRequest).getJson()))
                    .andReturn().getResponse();
        } catch (Exception e) {
            fail("Erro ao enviar req: " + e.getMessage());
            return null;
        }
    }
}

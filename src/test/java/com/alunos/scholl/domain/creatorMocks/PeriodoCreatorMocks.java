package com.alunos.scholl.domain.creatorMocks;


import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.CreatePeriodoResponse;
import com.alunos.scholl.domain.periodo.model.TipoPeriodo;
import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.response.CreateProfessorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@TestConfiguration
public class PeriodoCreatorMocks {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreatePeriodoRequest> createPeriodoRequestJacksonTester;

    @Autowired
    private JacksonTester<CreatePeriodoResponse> createPeriodoResponseJacksonTester;

    @Autowired
    private TestUtils testUtils;

    public CreatePeriodoResponse createRandomPeriodo() throws Exception {
        return createPeriodoRequest(
                new CreatePeriodoRequest(
                        TipoPeriodo.BIMESTRAL,
                        LocalDate.now().minusMonths(2),
                        LocalDate.now()
                )
        );
    }

    public CreatePeriodoResponse createPeriodoRequest(CreatePeriodoRequest
                                                                  createPeriodoRequest)
            throws Exception{
        var result = createPeriodo(createPeriodoRequest);

        return createPeriodoResponseJacksonTester.parseObject(result.getContentAsString());
    }

    public MockHttpServletResponse createPeriodo(CreatePeriodoRequest createPeriodoRequest) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post("/periodo/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    createPeriodoRequestJacksonTester
                                            .write(createPeriodoRequest).getJson()))
                    .andReturn().getResponse();
        } catch (Exception e) {
            fail("Erro ao enviar req: " + e.getMessage());
            return null;
        }
    }
}

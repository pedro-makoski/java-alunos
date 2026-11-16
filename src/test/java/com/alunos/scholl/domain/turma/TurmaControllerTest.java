package com.alunos.scholl.domain.turma;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.professor.dto.response.SampleProfessor;
import com.alunos.scholl.domain.turma.dto.response.SampleTurma;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@Transactional
@Import({
        AlunoCreatorMocks.class,
        TurmaCreatorMocks.class,
        MateriaCreatorMocks.class,
        ProfessorCreatorMocks.class,
        PeriodoCreatorMocks.class,
        TestUtils.class
})
@WithMockUser(authorities = "DONO")
class TurmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

    @Autowired
    private JacksonTester<SampleTurma> sampleTurmaJacksonTester;

    @Autowired
    private JacksonTester<PageMock<SampleTurma>> listOfTurmaJacksonTester;

    @Autowired
    private TurmaCreatorMocks turmaCreatorMocks;

    @Test
    @DisplayName("Validar se não aceita envios inválidos")
    void createInvalid() {
    }

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    void createInvalid2() {
    }

    @Test
    @DisplayName("Validar se envia e o objeto está enviado")
    void createValid() {
    }

    @Test
    @DisplayName("Envia um conjunto de turmas válidas e inválidas e verifica " +
            "se as válidas são retornadas")
    void getMyTurmas() {
    }

    @Test
    @DisplayName("Envia uma turma inválida e espera retornar um not found")
    void getProfessoresInvalidById() {
    }

    @Test
    @DisplayName("Envia uma turma válida e espera retorno com os dados corretos")
    void getPeriodoValidById() {
    }

    @Test
    @DisplayName("Envia um conjunto de turmas válidas de um professor e verifica se" +
            "apenas as do professor foram retornadas")
    void getMyTurmaByTeacher() {
    }

    @Test
    @DisplayName("Envia uma turma válida de um professor " +
            "e espera retorno como encontrado ")
    void getTurmaValidByIdOnTeacher() {
    }

    @Test
    @DisplayName("Envia uma turma válida que não é do professor " +
            "e espera retorno como não encontrado ")
    void getTurmaValidByIdOnNotTeacher() {
    }
}
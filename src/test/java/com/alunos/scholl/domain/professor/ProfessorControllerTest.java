package com.alunos.scholl.domain.professor;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
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
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

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
    @DisplayName("Envia um conjunto de professores válidos e inválidos e verifica " +
            "se os válidos são retornados")
    void getMyProfessores() {
    }

    @Test
    @DisplayName("Envia um professor inválido e espera retorno not found")
    void getProfessoresInvalidById() {
    }

    @Test
    @DisplayName("Envia um professor válido e espera retorno com os dados corretos")
    void getPeriodoValidById() {
    }
}
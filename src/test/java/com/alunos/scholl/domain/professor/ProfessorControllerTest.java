package com.alunos.scholl.domain.professor;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

    @Autowired
    private ProfessorCreatorMocks professorCreatorMocks;

    @Test
    @DisplayName("Validar se não aceita envios inválidos")
    void createInvalid() {
        var user = testUtils.addUserOnRequest();

        var professorInvalid1 = new CreateProfessorRequest(
                "emailwrong",
                "senhaaaaa",
                "Azul",
                "Batata",
                LocalDate.now().minusYears(20)
        );

        var professorInvalid2 = new CreateProfessorRequest(
                "email@email.com",
                "sen",
                "Azul",
                "Batata",
                LocalDate.now().minusYears(20)
        );

        var professorInvalid3 = new CreateProfessorRequest(
                "email@email.com",
                "senhaaaaa",
                "Azul",
                "Batata",
                LocalDate.now()
        );

        var professorInvalid4 = new CreateProfessorRequest(
                "email@email.com",
                "senhaaaaa",
                "Azul",
                "Batata",
                LocalDate.now()
        );

        var professors = List.of(professorInvalid1, professorInvalid2,
                professorInvalid3, professorInvalid4);

        var validation = professors.stream()
                .map(professorCreatorMocks::createProfessor).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    void createInvalid2() {
        var user = testUtils.addUserOnRequest();

        var professorInvalid1 = new CreateProfessorRequest(
                null,
                "senhaaaaa",
                "Azul",
                "Batata",
                LocalDate.now().minusYears(20)
        );

        var professorInvalid2 = new CreateProfessorRequest(
                "email@email.com",
                null,
                "Azul",
                "Batata",
                LocalDate.now().minusYears(20)
        );

        var professorInvalid3 = new CreateProfessorRequest(
                "email@email.com",
                "senhaaaaa",
                null,
                "Batata",
                LocalDate.now()
        );

        var professorInvalid4 = new CreateProfessorRequest(
                "email@email.com",
                "senhaaaaa",
                "Azul",
                null,
                LocalDate.now().minusYears(20)
        );

        var professorInvalid5 = new CreateProfessorRequest(
                "email@email.com",
                "senhaaaaa",
                "Azul",
                "Batata",
                null
        );

        var professors = List.of(professorInvalid1, professorInvalid2,
                professorInvalid3, professorInvalid4, professorInvalid5);

        var validation = professors.stream()
                .map(professorCreatorMocks::createProfessor).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }


    @Test
    @DisplayName("Validar se envia e o objeto está enviado")
    void createValid() {
        var user = testUtils.addUserOnRequest();

        var professorValid = new CreateProfessorRequest(
                "email@email.com",
                "senhaaaaa",
                "Azul",
                "Batata",
                LocalDate.now().minusYears(20)
        );

        var professor = professorCreatorMocks.createProfessor(professorValid);

        Assertions.assertEquals(HttpStatus.CREATED.value(), professor.getStatus());
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
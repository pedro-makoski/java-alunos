package com.alunos.scholl.domain.periodo;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.model.TipoPeriodo;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@WithMockUser(authorities = "DONO")
@Transactional
@Import({
        AlunoCreatorMocks.class,
        TurmaCreatorMocks.class,
        MateriaCreatorMocks.class,
        ProfessorCreatorMocks.class,
        PeriodoCreatorMocks.class,
        TestUtils.class
})
class PeriodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private PeriodoCreatorMocks periodoCreatorMocks;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    void createInvalid() {
        var user = testUtils.addUserOnRequest();

        var periodoInvalid1 = new CreatePeriodoRequest(
                null,
                testUtils.getRandomDate(),
                testUtils.getRandomDate()
        );

        var periodoInvalid2 = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                null,
                testUtils.getRandomDate()
        );

        var peridoInvalid3 = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                testUtils.getRandomDate(),
                null
        );

        var periodos = List.of(periodoInvalid1, periodoInvalid2, peridoInvalid3);

        var validation = periodos.stream().map(periodoCreatorMocks::createPeriodo).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se envia")
    void createValid() {
        var user = testUtils.addUserOnRequest();

        var peridoValid = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                testUtils.getRandomDate(),
                testUtils.getRandomDate()
        );

        var periodo = periodoCreatorMocks.createPeriodo(peridoValid);

        Assertions.assertEquals(HttpStatus.CREATED.value(), periodo.getStatus());
    }

    @Test
    @DisplayName("Envia um conjunto de períodos válidos e inválidos e verifica " +
            "se os válidos são retornados")
    void getMyPeriodos() {
    }

    @Test
    @DisplayName("Envia um período inválido e espera retorno not found")
    void getPeriodoInvalidById() {
    }

    @Test
    @DisplayName("Envia um período válido e espera retorno com os dados corretos")
    void getPeriodoValidById() {
    }

    @Test
    @DisplayName("Adiciona turma em um período e verifica se foi realmente adicionado")
    void addTurmaInPeriodo() {
    }

    @Test
    @DisplayName("Envia um conjunto de períodos válidos de um professor e verifica se" +
            "apenas a do professor foram retornadas")
    void getMyPeriodoByTeacher() {
    }

    @Test
    @DisplayName("Envia um período válido de um professor " +
            "e espera retorno como encontrado ")
    void getPeriodoValidByIdOnTeacher() {
    }

    @Test
    @DisplayName("Envia um período válido que não é do professor " +
            "e espera retorno como não encontrado ")
    void getPeridooValidByIdOnNotTeacher() {
    }

    @Test
    @DisplayName("Adiciona turma ao aluno e " +
            "verifica se ela foram realmente adicionadas")
    void getTurmaOfAluno() {
    }

    @Test
    @DisplayName("Adiciona período na matéria e " +
            "verifica se foi realmente adicionada")
    void addPeriodoInMateria() {
    }
}
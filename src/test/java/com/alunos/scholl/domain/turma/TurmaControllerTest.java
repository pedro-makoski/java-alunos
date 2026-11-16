package com.alunos.scholl.domain.turma;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.model.TipoPeriodo;
import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.dto.response.SampleProfessor;
import com.alunos.scholl.domain.turma.dto.request.CreateTurmaRequest;
import com.alunos.scholl.domain.turma.dto.response.SampleTurma;
import com.alunos.scholl.domain.turma.model.TipoTurma;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private ProfessorCreatorMocks professorCreatorMocks;

    @Autowired
    private MateriaCreatorMocks materiaCreatorMocks;

    @Test
    @DisplayName("Validar se não aceita envios inválidos")
    void createInvalid() {
        var user = testUtils.addUserOnRequest();

        var turmaInvalid1 = new CreateTurmaRequest(
                "a",
                TipoTurma.FUNDAMENTAL,
                20L
        );
        var turmaInvalid2 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.FUNDAMENTAL,
                -50L
        );

        var turmasInvalid = List.of(turmaInvalid1, turmaInvalid2);

        var validation = turmasInvalid.stream()
                .map(turmaCreatorMocks::createTurma).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    void createInvalid2() {
        var user = testUtils.addUserOnRequest();

        var turmaInvalid1 = new CreateTurmaRequest(
                null,
                TipoTurma.FUNDAMENTAL,
                20L
        );

        var turmaInvalid2 = new CreateTurmaRequest(
                "bccca",
                null,
                -50L
        );

        var turmaInvalid3 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                null
        );

        var turmasInvalid = List.of(turmaInvalid1, turmaInvalid2, turmaInvalid3);

        var validation = turmasInvalid.stream()
                .map(turmaCreatorMocks::createTurma).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se envia e o objeto está enviado")
    void createValid() throws Exception {
        var turmaValid = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var response = turmaCreatorMocks.createTurma(turmaValid);

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("Envia um conjunto de turmas válidas e inválidas e verifica " +
            "se as válidas são retornadas")
    void getMyTurmas() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaInvalid1 = new CreateTurmaRequest(
                null,
                TipoTurma.FUNDAMENTAL,
                20L
        );

        var turmaInvalid2 = new CreateTurmaRequest(
                "bccca",
                null,
                -50L
        );

        var turmaInvalid3 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                null
        );

        var turmaValid1 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turmaValid2 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turmaValid3 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turmaValid4 = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turmasInvalidos = List.of(turmaInvalid1,
                turmaInvalid2, turmaInvalid3);
        var turmasValidos = List.of(turmaValid1, turmaValid2, turmaValid3, turmaValid4);

        var turmas = new ArrayList<>(turmasInvalidos);

        turmas.addAll(turmasValidos);

        turmas.forEach(turmaCreatorMocks::createTurma);

        var professorResponse = mockMvc.perform(
                        MockMvcRequestBuilders.get("/turma"))
                .andReturn().getResponse();

        var response = listOfTurmaJacksonTester
                .parseObject(professorResponse.getContentAsString());

        Assertions.assertEquals(HttpStatus.OK.value(), professorResponse.getStatus());
        Assertions.assertEquals(turmasValidos.size(), response.totalElements());
    }

    @Test
    @DisplayName("Envia uma turma válida e espera retorno com os dados corretos")
    void getTurmaValidById() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaValid = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turma = turmaCreatorMocks.createTurmaRequest(turmaValid);

        var turmaGet = mockMvc.perform(
                MockMvcRequestBuilders.get("/turma/{turmaId}", turma.idTurma())
        ).andReturn().getResponse();

        var turmaGetDto = sampleTurmaJacksonTester
                .parseObject(turmaGet.getContentAsString());

        Assertions.assertEquals(turmaValid.nome(), turmaGetDto.nome());
        Assertions.assertEquals(turmaValid.tipoTurma(), turmaGetDto.tipoTurma());
        Assertions.assertEquals(turmaValid.tamanhoMaximo(), turmaGetDto.tamanhoMaximoTurma());
    }

    @Test
    @DisplayName("Envia um conjunto de turmas válidas de um professor e verifica se" +
            "apenas as do professor foram retornadas")
    void getMyTurmaByTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma1 = turmaCreatorMocks.createRandomTurma();
        var turma2 = turmaCreatorMocks.createRandomTurma();
        var turma3 = turmaCreatorMocks.createRandomTurma();

        var turmasOfTeacher = List.of(turma1, turma2);

        var professor = professorCreatorMocks.createRandomProfessor();
        var professor2 = professorCreatorMocks.createRandomProfessor();

        turmasOfTeacher.forEach(t -> {
            try {
                materiaCreatorMocks
                        .createRandomMateria(t.idTurma(), professor.professorId());
            } catch (Exception e) {
                fail(e.getMessage());
            }
        });

        testUtils.addProfessorOnRequest(professor.professorId());

        var turmaResponse = mockMvc.perform(
                        MockMvcRequestBuilders.get("/turma"))
                .andReturn().getResponse();

        System.out.println(turmaResponse.getContentAsString()+" -- azuç");

        var response = listOfTurmaJacksonTester
                .parseObject(turmaResponse.getContentAsString());

        var ids = response.content().stream()
                .map(SampleTurma::id).toList();

        Assertions.assertEquals(turmasOfTeacher.size(),
                response.totalElements());

        turmasOfTeacher.forEach(p -> {
            Assertions.assertEquals(1, ids.stream()
                    .filter(i -> p.idTurma().equals(i)).toList().size());
        });
    }

    @Test
    @DisplayName("Envia uma turma válida de um professor " +
            "e espera retorno como encontrado ")
    void getTurmaValidByIdOnTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var createTurmaRequest = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turma = turmaCreatorMocks.createTurmaRequest(createTurmaRequest);

        var professor = professorCreatorMocks.createRandomProfessor();

        var materia = materiaCreatorMocks.
                createRandomMateria(turma.idTurma(), professor.professorId());


        testUtils.addProfessorOnRequest(professor.professorId());

        var turmaGet = mockMvc
                .perform(MockMvcRequestBuilders.get(
                        "/turma/{id}", turma.idTurma()))
                .andReturn().getResponse();

        var result = sampleTurmaJacksonTester
                .parseObject(turmaGet.getContentAsString());

        Assertions.assertEquals(createTurmaRequest.nome(), result.nome());
        Assertions.assertEquals(createTurmaRequest.tipoTurma(), result.tipoTurma());
        Assertions.assertEquals(createTurmaRequest.tamanhoMaximo(), result.tamanhoMaximoTurma());
    }

    @Test
    @DisplayName("Envia uma turma válida que não é do professor " +
            "e espera retorno como não encontrado ")
    void getTurmaValidByIdOnNotTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var createTurmaRequest = new CreateTurmaRequest(
                "bccca",
                TipoTurma.MEDIO,
                20L
        );

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();


        testUtils.addProfessorOnRequest(professor.professorId());

        var turmaGet = mockMvc
                .perform(MockMvcRequestBuilders.get(
                        "/turma/{id}", turma.getId()))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), turmaGet.getStatus());
    }
}
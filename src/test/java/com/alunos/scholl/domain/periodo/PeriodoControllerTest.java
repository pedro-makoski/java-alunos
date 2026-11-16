package com.alunos.scholl.domain.periodo;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.periodo.dto.request.CreatePeriodoRequest;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.periodo.exceptions.PeriodoNotFound;
import com.alunos.scholl.domain.periodo.model.TipoPeriodo;
import com.alunos.scholl.domain.periodo.model.TurmaPeriodoId;
import com.alunos.scholl.domain.periodo.repositories.PeriodoRepository;
import com.alunos.scholl.domain.periodo.repositories.TurmaPeriodoRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import org.hibernate.query.Page;
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

    @Autowired
    private TurmaCreatorMocks turmaCreatorMocks;

    @Autowired
    private JacksonTester<PageMock<PeriodoSample>> pageMockJacksonTesterOfPeriodo;

    @Autowired
    private JacksonTester<PeriodoSample> periodoSampleJacksonTester;

    @Autowired
    private TurmaPeriodoRepository turmaPeriodoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private ProfessorCreatorMocks professorCreatorMocks;

    @Autowired
    private MateriaCreatorMocks materiaCreatorMocks;

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
    void getMyPeriodos() throws Exception {
        var user = testUtils.addUserOnRequest();

        var periodoInvalid1 = new CreatePeriodoRequest(
                null,
                testUtils.getRandomDate(),
                testUtils.getRandomDate()
        );

        var periodoInvalid2 = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                testUtils.getRandomDate(),
                null
        );

        var periodoValid1 = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                testUtils.getRandomDate(),
                testUtils.getRandomDate()
        );

        var periodoValid2 = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                testUtils.getRandomDate(),
                testUtils.getRandomDate()
        );;

        var periodoValid3 = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                testUtils.getRandomDate(),
                testUtils.getRandomDate()
        );

        var periodosInvalidos = List.of(periodoInvalid1, periodoInvalid2);
        var periodosValidos = List.of(periodoValid1, periodoValid2, periodoValid3);

        var periodos = new ArrayList<>(periodosInvalidos);

        periodos.addAll(periodosValidos);

        periodos.forEach(periodoCreatorMocks::createPeriodo);

        var periodoResponse = mockMvc.perform(
                        MockMvcRequestBuilders.get("/periodo"))
                .andReturn().getResponse();


        var response = pageMockJacksonTesterOfPeriodo
                .parseObject(periodoResponse.getContentAsString());

        Assertions.assertEquals(HttpStatus.OK.value(), periodoResponse.getStatus());
        Assertions.assertEquals(periodosValidos.size(), response.totalElements());
    }

    @Test
    @DisplayName("Envia um período válido e espera retorno com os dados corretos")
    void getPeriodoValidById() throws Exception {
        var user = testUtils.addUserOnRequest();

        var periodoDto = new CreatePeriodoRequest(
                TipoPeriodo.BIMESTRAL,
                LocalDate.now().minusMonths(2),
                LocalDate.now()
        );

        var periodo = periodoCreatorMocks.createPeriodoRequest(periodoDto);

        var periodoGet = mockMvc
                .perform(MockMvcRequestBuilders.get(
                        "/periodo/{id}", periodo.idPeriodo().toString()))
                .andReturn().getResponse();

        var result = periodoSampleJacksonTester.parseObject(periodoGet.getContentAsString());

        Assertions.assertEquals(HttpStatus.OK.value(), periodoGet.getStatus());
        Assertions.assertEquals(periodoDto.tipoPeriodo(), result.tipoPeriodo());
        Assertions.assertEquals(periodoDto.inicio(), result.inicio());
        Assertions.assertEquals(periodoDto.fim(), result.fim());

    }

    @Test
    @DisplayName("Adiciona turma em um período e verifica se foi realmente adicionado")
    void addTurmaInPeriodo() throws Exception {
        var user = testUtils.addUserOnRequest();
        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var periodo = periodoCreatorMocks.createRandomPeriodo();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/periodo/add-turma-in-periodo/{periodoId}", periodo.idPeriodo())
                        .queryParam("turmaId", turma.getId().toString())
        );

        var periodoEntity = periodoRepository.findById(periodo.idPeriodo())
                    .orElseThrow(PeriodoNotFound::new);

        var res = turmaPeriodoRepository
                    .findById(new TurmaPeriodoId(periodoEntity, turma)).isPresent();

        Assertions.assertTrue(res);
    }

    @Test
    @DisplayName("Envia um conjunto de períodos válidos de um professor e verifica se" +
            "apenas a do professor foram retornadas")
    void getMyPeriodoByTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var periodo1 = periodoCreatorMocks.createRandomPeriodo();
        var periodo2 = periodoCreatorMocks.createRandomPeriodo();
        var periodo3 = periodoCreatorMocks.createRandomPeriodo();

        var periodosOfTeacher = List.of(periodo1, periodo2);

        var professor = professorCreatorMocks.createRandomProfessor();
        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var materia = materiaCreatorMocks
                .createRandomMateria(turma.getId(), professor.professorId());

        periodosOfTeacher.forEach(periodo -> {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch("/periodo/add-turma-in-periodo/{periodoId}", periodo.idPeriodo())
                                .queryParam("turmaId", turma.getId().toString())
                );
            } catch (Exception e) {
                fail(e.getMessage());
            }
        });

        testUtils.addProfessorOnRequest(professor.professorId());

        var periodoResponse = mockMvc.perform(
                        MockMvcRequestBuilders.get("/periodo"))
                .andReturn().getResponse();

        System.out.println(periodoResponse);

        var response = pageMockJacksonTesterOfPeriodo
                .parseObject(periodoResponse.getContentAsString());

        var ids = response.content().stream()
                .map(PeriodoSample::idPeriodo).toList();

        Assertions.assertEquals(periodosOfTeacher.size(),
                response.totalElements());

        periodosOfTeacher.forEach(p -> {
            Assertions.assertEquals(1, ids.stream()
                    .filter(i -> p.idPeriodo().equals(i)).toList().size());
        });
    }

    @Test
    @DisplayName("Envia um período válido de um professor " +
            "e espera retorno como encontrado ")
    void getPeriodoValidByIdOnTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var createPeriodoRequest = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                LocalDate.now().minusMonths(3),
                LocalDate.now()
        );

        var periodo = periodoCreatorMocks.createPeriodoRequest(createPeriodoRequest);

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();
        var materia = materiaCreatorMocks.
                    createRandomMateria(turma.getId(), professor.professorId());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/periodo/add-turma-in-periodo/{periodoId}", periodo.idPeriodo())
                        .queryParam("turmaId", turma.getId().toString())
        );

        testUtils.addProfessorOnRequest(professor.professorId());

        var periodoGet = mockMvc
                .perform(MockMvcRequestBuilders.get(
                        "/periodo/{id}", periodo.idPeriodo()))
                .andReturn().getResponse();

        var result = periodoSampleJacksonTester
                    .parseObject(periodoGet.getContentAsString());

        Assertions.assertEquals(createPeriodoRequest.tipoPeriodo(), result.tipoPeriodo());
        Assertions.assertEquals(createPeriodoRequest.inicio(), result.inicio());
        Assertions.assertEquals(createPeriodoRequest.fim(), result.fim());

    }

    @Test
    @DisplayName("Envia um período válido que não é do professor " +
            "e espera retorno como não encontrado ")
    void getPeridooValidByIdOnNotTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var createPeriodoRequest = new CreatePeriodoRequest(
                TipoPeriodo.ANUAL,
                LocalDate.now().minusMonths(3),
                LocalDate.now()
        );

        var periodo = periodoCreatorMocks.createPeriodoRequest(createPeriodoRequest);

        var professor = professorCreatorMocks.createRandomProfessor();

        testUtils.addProfessorOnRequest(professor.professorId());

        var periodoGet = mockMvc
                .perform(MockMvcRequestBuilders.get(
                        "/periodo/{id}", periodo.idPeriodo()))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),periodoGet.getStatus());
    }
}
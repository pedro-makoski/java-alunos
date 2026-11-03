package com.alunos.scholl.domain.aluno;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.AlunoDetails;
import com.alunos.scholl.domain.aluno.dto.response.AlunoSample;
import com.alunos.scholl.domain.aluno.dto.response.GetNotaAlunoResponse;
import com.alunos.scholl.domain.aluno.model.DetailsNota;
import com.alunos.scholl.domain.aluno.repositories.AlunoRepository;
import com.alunos.scholl.domain.creatorMocks.*;

import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

    @Autowired
    private JacksonTester<PageMock<AlunoSample>>  alunoSampleListJson;

    @Autowired
    private AlunoCreatorMocks alunoCreatorMocks;

    @Autowired
    private TurmaCreatorMocks turmaCreatorMocks;

    @Autowired
    private MateriaCreatorMocks materiaCreatorMocks;

    @Autowired
    private ProfessorCreatorMocks professorCreatorMocks;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private JacksonTester<AlunoSample> alunoSampleJacksonTester;

    @Autowired
    private PeriodoCreatorMocks periodoCreatorMocks;

    @Autowired
    private JacksonTester<GetNotaAlunoResponse> getNotaAlunoResponseJacksonTester;

    @Autowired
    private JacksonTester<AlunoDetails> alunoDetailsJacksonTester;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("Validar se não aceita envios inválidos")
    void createInvalid() throws Exception {
        var user = testUtils.addUserOnRequest();
        var turmaBase = turmaCreatorMocks.createTurmaRandom(user);

        var userInvalid1 = new CreateAlunoRequest(
                turmaBase.getId(),
                "",
                LocalDate.now().minusYears(20),
                "PAI",
                "MÃE"
        );

        var userInvalid2 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().plusYears(30),
                "PAI",
                "MÃE"
        );

        var userInvalid3 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "",
                ""
        );

        var users = List.of(userInvalid1, userInvalid2, userInvalid3);

        var validation = users.stream().map(alunoCreatorMocks::sendAlunoRequest).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    void createInvalid2() throws Exception {
        var user = testUtils.addUserOnRequest();
        var turmaBase = turmaCreatorMocks.createTurmaRandom(user);

        var userInvalid1 = new CreateAlunoRequest(
                null,
                "aaaaaa",
                LocalDate.now().minusYears(20),
                "PAI",
                "MÃE"
        );

        var userInvalid2 = new CreateAlunoRequest(
                turmaBase.getId(),
                null,
                LocalDate.now().minusYears(30),
                "PAI",
                "MÃE"
        );

        var userInvalid3 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                null,
                "aaa",
                "bbb"
        );

        var userInvalid4 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                null,
                "aaa"
        );

        var userInvalid5 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                null
        );

        var users = List.of(userInvalid1, userInvalid2, userInvalid3, userInvalid4, userInvalid5);

        var validation = users.stream().map(alunoCreatorMocks::sendAlunoRequest).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se envia")
    void createValid() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaCreatorMocks.createTurmaRandom(user);

        var userValid = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var res = alunoCreatorMocks.sendAlunoRequest(userValid);

        Assertions.assertEquals(HttpStatus.CREATED.value(), res.getStatus());
    }


    @Test
    @DisplayName("Envia um conjunto de alunos válidos e inválidos e verifica se eles" +
            "se os válidos são retornados")
    void getMyAlunos() throws Exception{
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaCreatorMocks.createTurmaRandom(user);

        var userInvalid1 = new CreateAlunoRequest(
                turmaBase.getId(),
                "",
                LocalDate.now().minusYears(20),
                "PAI",
                "MÃE"
        );

        var userInvalid2 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().plusYears(30),
                "PAI",
                "MÃE"
        );

        var userValid1 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var userValid2 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var userValid3 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var userValid4 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var userInvalid3 = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "",
                ""
        );

        var alunosInvalidos = List.of(userInvalid1, userInvalid2, userInvalid3);
        var alunosValido = List.of(userValid1, userValid2, userValid3, userValid4);

        var alunos = new ArrayList<>(alunosValido);

        alunos.addAll(alunosInvalidos);

        alunos.forEach(alunoCreatorMocks::sendAlunoRequest);

        var alunosResponse = mockMvc.perform(
                MockMvcRequestBuilders.get("/aluno"))
                .andReturn().getResponse();


        var response = alunoSampleListJson.parseObject(alunosResponse.getContentAsString());

        Assertions.assertEquals(HttpStatus.OK.value(), alunosResponse.getStatus());
        Assertions.assertEquals(alunosValido.size(), response.totalElements());

    }

    @Test
    @DisplayName("Envia um conjunto de alunos válidos de um professor e de um que não é" +
            "e valida de se retorna os do professor")
    void getMyAlunosByTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var QUANT_USERS_WITH_TEACHER = 5;
        var QUANT_USERS_WITHOUT_TEACHER = 2;

        var turma = turmaCreatorMocks.createTurmaRandom(user);

        alunoCreatorMocks.createMultiplesAlunos(user,
                            QUANT_USERS_WITH_TEACHER, turma.getId());

        alunoCreatorMocks.createMultiplesAlunos(user, QUANT_USERS_WITHOUT_TEACHER);

        var professor = professorCreatorMocks.createRandomProfessor();

        materiaCreatorMocks.createRandomMateria(
                turma.getId(),
                professor.professorId()
        );

        testUtils.addProfessorOnRequest(professor.professorId());

        var returnItens = mockMvc.perform(
                MockMvcRequestBuilders.get("/aluno")
        ).andReturn().getResponse();

        var result = alunoSampleListJson.parseObject(returnItens.getContentAsString());

        Assertions.assertEquals(QUANT_USERS_WITH_TEACHER, result.totalElements());
    }

    @Test
    @DisplayName("Envia um aluno válidos e espera retorno com os dados corretos")
    void getAlunoValidById() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var alunoDto = new CreateAlunoRequest(
                turma.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var aluno = alunoCreatorMocks.createAluno(alunoDto);

        var alunoGet = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/aluno/%s", aluno.idAluno()))
        ).andReturn().getResponse();

        var alunoGetDto = alunoSampleJacksonTester.parseObject(alunoGet.getContentAsString());

        Assertions.assertEquals(alunoDto.nome(), alunoGetDto.nome());
        Assertions.assertEquals(alunoDto.nomeMae(), alunoGetDto.nomeMae());
        Assertions.assertEquals(alunoDto.nomePai(), alunoGetDto.nomePai());
        Assertions.assertEquals(alunoDto.dataNascimento(), alunoGetDto.dataNascimento());
    }

    @Test
    @DisplayName("Envia um aluno válido de um professor " +
            "e espera retorno como encontrado ")
    void getAlunoValidByIdOnTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var alunoDto = new CreateAlunoRequest(
                turma.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var aluno = alunoCreatorMocks.createAluno(alunoDto);

        var professor = professorCreatorMocks.createRandomProfessor();

        materiaCreatorMocks.createRandomMateria(turma.getId(), professor.professorId());

        testUtils.addProfessorOnRequest(professor.professorId());

        var alunoGet = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/aluno/%s", aluno.idAluno()))
        ).andReturn().getResponse();

        var alunoGetDto = alunoSampleJacksonTester.parseObject(alunoGet.getContentAsString());

        Assertions.assertEquals(alunoDto.nome(), alunoGetDto.nome());
        Assertions.assertEquals(alunoDto.nomeMae(), alunoGetDto.nomeMae());
        Assertions.assertEquals(alunoDto.nomePai(), alunoGetDto.nomePai());
        Assertions.assertEquals(alunoDto.dataNascimento(), alunoGetDto.dataNascimento());
    }

    @Test
    @DisplayName("Envia um aluno válido que não é do professor " +
            "e espera retorno como não encontrado ")
    void getAlunoValidByIdOnNotTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var alunoDto = new CreateAlunoRequest(
                turma.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var aluno = alunoCreatorMocks.createAluno(alunoDto);

        var professor = professorCreatorMocks.createRandomProfessor();

        testUtils.addProfessorOnRequest(professor.professorId());

        var alunoGet = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/aluno/%s", aluno.idAluno()))
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), alunoGet.getStatus());
    }

    @Test
    @DisplayName("Altera a turma do aluno e verifica a turma deles")
    void updateAlunoTurma() throws Exception {
        var user = testUtils.addUserOnRequest();

        var aluno = alunoCreatorMocks.createAlunoRandom(user);
        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var alunoTurmaBefore = alunoRepository
                    .getReferenceById(aluno.idAluno()).getTurma().getId();

        var response = mockMvc.perform(
                MockMvcRequestBuilders.patch("/aluno/turma/change")
                        .queryParam("alunoId", aluno.idAluno().toString())
                        .queryParam("turmaId", turma.getId().toString())
        ).andReturn().getResponse();

        var alunoGetRepository = alunoRepository.getReferenceById(aluno.idAluno());

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Assertions.assertEquals(turma.getId(), alunoGetRepository.getTurma().getId());
        Assertions.assertNotEquals(alunoTurmaBefore, alunoGetRepository.getTurma().getId());
    }

    @Test
    @DisplayName("Envia um ID que não existe de um aluno e verifica se retorna erro")
    void updateAlunoTurmaInvalid() throws Exception {
        var user = testUtils.addUserOnRequest();

        var aluno = alunoCreatorMocks.createAlunoRandom(user);

        var alunoTurmaBefore = alunoRepository.getReferenceById(aluno.idAluno()).getTurma().getId();

        var response = mockMvc.perform(
                MockMvcRequestBuilders.patch("/aluno/turma/change")
                        .queryParam("alunoId", aluno.idAluno().toString())
                        .queryParam("turmaId", UUID.randomUUID().toString())
        ).andReturn().getResponse();

        var alunoNow = alunoRepository.getReferenceById(aluno.idAluno());

        var alunoTurmaNow = alunoNow.getTurma().getId();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Assertions.assertEquals(alunoTurmaBefore, alunoTurmaNow);
    }

    @Test
    @DisplayName("Cria aluno e adiciona notas com dados válidos e inválidos " +
            "e verifica se retorna corretamente")
    void getAlunoDetailsAndNota() throws Exception {
        var user = testUtils.addUserOnRequest();

        var professor = professorCreatorMocks.createRandomProfessor();
        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var aluno = alunoCreatorMocks.createAlunoRandom(user, turma.getId());

        var materia = materiaCreatorMocks
                .createRandomMateria(turma.getId(), professor.professorId());

        var perido = periodoCreatorMocks.
                createRandomPeriodo();

        var periodo2 = periodoCreatorMocks.createRandomPeriodo();

        testUtils.addProfessorOnRequest(professor.professorId());

        Double NOTA_VALID_1 = 50.2;
        Double NOTA_VALID_2 = 40.3;

        var notaMedia = (NOTA_VALID_1+NOTA_VALID_2)/2;

        var notaValid1 = alunoCreatorMocks.createNota(new AddNotaRequest(
                                        NOTA_VALID_1,
                                        List.of(new DetailsNota(testUtils.getRandomNome(),50.2)),
                                        aluno.idAluno(),
                                        perido.idPeriodo(),
                                        materia.materiaId()
                                ));

        var notaValid2 = alunoCreatorMocks.createNota(new AddNotaRequest(
                NOTA_VALID_2,
                List.of(new DetailsNota(testUtils.getRandomNome(),50.2)),
                aluno.idAluno(),
                periodo2.idPeriodo(),
                materia.materiaId()
        ));

        var notaInvalid1 = alunoCreatorMocks.createNota(new AddNotaRequest(
                                        -50.0,
                                        List.of(new DetailsNota(testUtils.getRandomNome(),50.2)),
                                        aluno.idAluno(),
                                        perido.idPeriodo(),
                                        materia.materiaId()
                                ));

        var notaInvalid2 = alunoCreatorMocks.createNota(new AddNotaRequest(
                100.5,
                List.of(new DetailsNota(testUtils.getRandomNome(),50.2)),
                aluno.idAluno(),
                perido.idPeriodo(),
                materia.materiaId()
        ));

        var professor2 = professorCreatorMocks.createRandomProfessor();

        testUtils.addProfessorOnRequest(professor2.professorId());

        var notaForbidden = alunoCreatorMocks.createNota(new AddNotaRequest(
                50.2,
                List.of(new DetailsNota(testUtils.getRandomNome(),50.2)),
                aluno.idAluno(),
                perido.idPeriodo(),
                materia.materiaId()
        ));

        var notaGetResultForbidden = mockMvc.perform(
                MockMvcRequestBuilders.get("/aluno/nota-aluno/details")
                        .queryParam("idAluno", aluno.idAluno().toString())
                        .queryParam("idMateria", materia.materiaId().toString())
                        .queryParam("idPeriodo", perido.idPeriodo().toString())
        ).andReturn().getResponse();

        var notaOverviewForbidden = mockMvc.perform(
                MockMvcRequestBuilders.get("/aluno/overview/{id}", aluno.idAluno())
        ).andReturn().getResponse();

        testUtils.addProfessorOnRequest(professor.professorId());

        var notaGetResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/aluno/nota-aluno/details")
                        .queryParam("idAluno", aluno.idAluno().toString())
                        .queryParam("idMateria", materia.materiaId().toString())
                        .queryParam("idPeriodo", perido.idPeriodo().toString())

        ).andReturn().getResponse();

        var notaGetResultBody = getNotaAlunoResponseJacksonTester
                .parseObject(notaGetResult.getContentAsString());

        var notaOverview = mockMvc.perform(
                MockMvcRequestBuilders.get("/aluno/overview/{id}", aluno.idAluno())
        ).andReturn().getResponse();

        var notaOverviewBody = alunoDetailsJacksonTester
                    .parseObject(notaOverview.getContentAsString());

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), notaValid1.getStatus());
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), notaValid2.getStatus());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), notaInvalid1.getStatus());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), notaInvalid2.getStatus());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), notaForbidden.getStatus());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),
                notaGetResultForbidden.getStatus());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),
                notaOverviewForbidden.getStatus());

        Assertions.assertEquals(NOTA_VALID_1, notaGetResultBody.nota());
        Assertions.assertEquals(notaMedia, notaOverviewBody.notaMedia());
        Assertions.assertEquals(aluno.idAluno(), notaOverviewBody.idAluno());
    }
}
package com.alunos.scholl.domain.materia;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.aluno.dto.request.AddNotaRequest;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.AlunoSample;
import com.alunos.scholl.domain.aluno.dto.response.GridNotas;
import com.alunos.scholl.domain.creatorMocks.*;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
import com.alunos.scholl.domain.materia.repositories.MateriaRepository;
import com.alunos.scholl.domain.periodo.dto.response.PeriodoSample;
import com.alunos.scholl.domain.turma.repositories.TurmaRepository;
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
import java.util.UUID;

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
class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private MateriaCreatorMocks materiaCreatorMocks;

    @Autowired
    private ProfessorCreatorMocks professorCreatorMocks;

    @Autowired
    private AlunoCreatorMocks alunoCreatorMocks;

    @Autowired
    private TurmaCreatorMocks turmaCreatorMocks;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

    @Autowired
    private JacksonTester<PageMock<MateriaSample>> materiaSamplesListJson;

    @Autowired
    private JacksonTester<MateriaSample> materiaSampleJacksonTester;

    @Autowired
    private JacksonTester<GridNotas> notasOfAluno;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private PeriodoCreatorMocks periodoCreatorMocks;

    @Autowired
    private JacksonTester<PageMock<PeriodoSample>> periodoSampleJson;

    @Test
    @DisplayName("Validar se não aceita envios inválidos")
    void createInvalid() throws Exception {
        var user = testUtils.addUserOnRequest();

        var professor = professorCreatorMocks.createRandomProfessor();
        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var materiaInvalid1 = new CreateMateriaRequest(
                "a",
                professor.professorId(),
                turma.getId()
        );


        var materias = List.of(materiaInvalid1);

        var validation = materias.stream().map(materiaCreatorMocks::createMateria).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    void createInvalid2() throws Exception {
        var user = testUtils.addUserOnRequest();

        var professor = professorCreatorMocks.createRandomProfessor();
        var turma = turmaCreatorMocks.createTurmaRandom(user);

        var materiaInvalid1 = new CreateMateriaRequest(
                null,
                professor.professorId(),
                turma.getId()
        );


        var materias = List.of(materiaInvalid1);

        var validation = materias.stream().map(materiaCreatorMocks::createMateria).toList();

        validation.forEach((v) ->
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), v.getStatus()));
    }

    @Test
    @DisplayName("Validar se envia e o objeto está enviado")
    void createValid() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();

        var materiaValid = new CreateMateriaRequest(
                testUtils.getRandomNome(),
                professor.professorId(),
                turmaBase.getId()
        );

        var res = materiaCreatorMocks.createMateria(materiaValid);

        Assertions.assertEquals(HttpStatus.CREATED.value(), res.getStatus());
    }

    @Test
    @DisplayName("Envia um conjunto de matérias válidas e inválidas e verifica se os" +
            "dados válidos são retornados")
    void getMyMaterias() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();

        var materiaInvalid1 = new CreateMateriaRequest(
                null,
                professor.professorId(),
                turmaBase.getId()
        );

        var materiaInvalid2 = new CreateMateriaRequest(
                "a",
                professor.professorId(),
                turmaBase.getId()
        );

        var materiaValid1 = new CreateMateriaRequest(
                "fjdjfklsdjf",
                professor.professorId(),
                turmaBase.getId()
        );

        var materiaValid2 = new CreateMateriaRequest(
                "vkflkgflkdglfkgkdflç",
                professor.professorId(),
                turmaBase.getId()
        );

        var materiaValid3 = new CreateMateriaRequest(
                "kdlkgldkglfgkfdlçgfd",
                professor.professorId(),
                turmaBase.getId()
        );

        var materiasInvalidos = List.of(materiaInvalid1, materiaInvalid2);
        var materiasValido = List.of(materiaValid1, materiaValid2, materiaValid3);

        var materias = new ArrayList<>(materiasValido);

        materias.addAll(materiasInvalidos);

        materias.forEach(materiaCreatorMocks::createMateria);

        var materiaResponse = mockMvc.perform(
                        MockMvcRequestBuilders.get("/materia"))
                .andReturn().getResponse();


        var response = materiaSamplesListJson
                    .parseObject(materiaResponse.getContentAsString());

        Assertions.assertEquals(HttpStatus.OK.value(), materiaResponse.getStatus());
        Assertions.assertEquals(alunosValido.size(), response.totalElements());
    }

    @Test
    @DisplayName("Envia um aluno válido e espera retorno com os dados corretos")
    void getMateriaValidById() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();

        var materiaDto = new CreateMateriaRequest(
                testUtils.getRandomNome(),
                professor.professorId(),
                turma.getId()
        );

        var materia = materiaCreatorMocks.createMateriaConverted(materiaDto);

        var materiaGet = mockMvc.perform(
                MockMvcRequestBuilders.get("/materia/{id}", materia.materiaId())
        ).andReturn().getResponse();

        var materiaGetDto = materiaSampleJacksonTester
                    .parseObject(materiaGet.getContentAsString());

        Assertions.assertEquals(materiaDto.nome(), materiaGetDto.nome());

    }

    @Test
    @DisplayName("Envia uma matéria válida de um professor " +
            "e espera retorno como encontrado ")
    void getMateriaValidByIdOnTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();

        var materiaDto = new CreateMateriaRequest(
                testUtils.getRandomNome(),
                professor.professorId(),
                turma.getId()
        );

        var materia = materiaCreatorMocks.createMateriaConverted(materiaDto);

        testUtils.addProfessorOnRequest(professor.professorId());

        var materiaGet = mockMvc.perform(
                MockMvcRequestBuilders.get("/materia/{id}", materia.materiaId())
        ).andReturn().getResponse();

        var materiaGetDto = materiaSampleJacksonTester
                .parseObject(materiaGet.getContentAsString());

        Assertions.assertEquals(materiaDto.nome(), materiaGetDto.nome());
    }

    @Test
    @DisplayName("Envia uma matéria válido que não é do professor " +
            "e espera retorno como não encontrado ")
    void getMateiraValidByIdOnNotTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();

        var materiaDto = new CreateMateriaRequest(
                testUtils.getRandomNome(),
                professor.professorId(),
                turma.getId()
        );

        var materia = materiaCreatorMocks.createMateriaConverted(materiaDto);

        var professor2 = professorCreatorMocks.createRandomProfessor();

        testUtils.addProfessorOnRequest(professor2.professorId());

        var materiaGet = mockMvc.perform(
                MockMvcRequestBuilders.get("/materia/{id}", materia.materiaId())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), materiaGet.getStatus());
    }


    @Test
    @DisplayName("Altera matéria da turma e verifica se foi realmente trocado")
    void changeTurmaInMateria() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();
        var materia = materiaCreatorMocks.createRandomMateria(turma.getId(), professor.professorId());

        var turma2 = turmaCreatorMocks.createTurmaRandom(user);

        var response = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/materia/change-turma-materia/{materiaId}",
                                materia.materiaId())
                        .queryParam("turmaId", turma2.getId().toString())
        ).andReturn().getResponse();

        var materiaNow = materiaRepository.getReferenceById(materia.materiaId());

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Assertions.assertEquals(turma2.getId(), materiaNow.getTurma().getId());
    }

    @Test
    @DisplayName("Adiciona nota aos alunos e verifica se " +
            "elas foram realmente adicionada")
    void getNotasOfMateria() throws Exception {
        var user = testUtils.addUserOnRequest();

        var professor = professorCreatorMocks.createRandomProfessor();
        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var materia = materiaCreatorMocks
                .createRandomMateria(turma.getId(), professor.professorId());
        var periodo = periodoCreatorMocks.createRandomPeriodo();

        var enemyTeacher = professorCreatorMocks.createRandomProfessor();

        var aluno1 = alunoCreatorMocks.createAlunoRandomAndReturnDto(user, turma.getId());
        var aluno2 = alunoCreatorMocks.createAlunoRandomAndReturnDto(user, turma.getId());
        var aluno3 = alunoCreatorMocks.createAlunoRandomAndReturnDto(user, turma.getId());

        var alunos = List.of(aluno1, aluno2, aluno3);

        var notaAluno1 = 35.0;
        var notaAluno2 = 70.0;
        var notaAluno3 = 40.0;

        var quantAlunos = 3;

        var notaAlunoWrong = 51.0;

        testUtils.addProfessorOnRequest(professor.professorId());

        alunoCreatorMocks.createNota(new AddNotaRequest(
                notaAluno1,
                List.of(),
                aluno1.idAluno(),
                periodo.idPeriodo(),
                materia.materiaId()
        ));

        alunoCreatorMocks.createNota(new AddNotaRequest(
                notaAluno2,
                List.of(),
                aluno2.idAluno(),
                periodo.idPeriodo(),
                materia.materiaId()
        ));

        alunoCreatorMocks.createNota(new AddNotaRequest(
                notaAluno3,
                List.of(),
                aluno3.idAluno(),
                periodo.idPeriodo(),
                materia.materiaId()
        ));

        testUtils.addProfessorOnRequest(enemyTeacher.professorId());

        var wrongRequest = alunoCreatorMocks.createNota(new AddNotaRequest(
                notaAlunoWrong,
                List.of(),
                aluno2.idAluno(),
                periodo.idPeriodo(),
                materia.materiaId()
        ));

        testUtils.addProfessorOnRequest(professor.professorId());

        var reqToGet = mockMvc.perform(
                MockMvcRequestBuilders.get("/materia/grid-materias/get")
                        .queryParam("periodoId", periodo.idPeriodo().toString())
                        .queryParam("materiaId", materia.materiaId().toString())
        ).andReturn().getResponse();

        var reqConverted = notasOfAluno.parseObject(reqToGet.getContentAsString());

        testUtils.addProfessorOnRequest(enemyTeacher.professorId());

        var reqForbidden = mockMvc.perform(
                MockMvcRequestBuilders.get("/materia/grid-materias/get")
                        .queryParam("periodoId", periodo.idPeriodo().toString())
                        .queryParam("materiaId", materia.materiaId().toString())
        ).andReturn().getResponse();


        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), wrongRequest.getStatus());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), reqForbidden.getStatus());

        Assertions.assertEquals(quantAlunos, reqConverted.userItemList().size());

        reqConverted.userItemList()
                .forEach(u -> {
                    var alunoFinded = alunos.stream()
                            .map(a -> a.nome())
                                    .toList().indexOf(u.nome());

                    switch(alunoFinded) {
                        case 0:
                            Assertions.assertEquals(notaAluno1, u.nota());
                            break;
                        case 1:
                            Assertions.assertEquals(notaAluno2, u.nota());
                            break;
                        case 2:
                            Assertions.assertEquals(notaAluno3, u.nota());
                            break;
                        default:
                            fail("Usuário inválido adicionado no meio");
                    }
                });
    }

    @Test
    @DisplayName("Adiciona períodos na matéria e verifica " +
            "se eles foram realmente adicionados")
    void getPeriodosOfMateria() throws Exception {
        var user = testUtils.addUserOnRequest();
        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var professor = professorCreatorMocks.createRandomProfessor();

        var materia = materiaCreatorMocks
                    .createRandomMateria(turma.getId(), professor.professorId());

        var periodo1 = periodoCreatorMocks.createRandomPeriodo();
        var periodo2 = periodoCreatorMocks.createRandomPeriodo();
        var periodo3 = periodoCreatorMocks.createRandomPeriodo();
        periodoCreatorMocks.createRandomPeriodo();

        var periodos = List.of(periodo1, periodo2, periodo3);

        addPeriodoInMateria(periodo1.idPeriodo(), materia.materiaId());
        addPeriodoInMateria(periodo2.idPeriodo(), materia.materiaId());
        addPeriodoInMateria(periodo3.idPeriodo(), materia.materiaId());

        var response = mockMvc
                .perform(MockMvcRequestBuilders.get("/materia/grid-materia/get-periodos")
                        .queryParam("idMateria", materia.materiaId().toString())
                )
                .andReturn().getResponse();

        var responseDto = periodoSampleJson.parseObject(response.getContentAsString());

        Assertions.assertEquals(periodos.size(), responseDto.totalElements());

        responseDto.content()
                .forEach(i -> {
                    var item = periodos.stream().filter(p -> p.idPeriodo().equals(i.idPeriodo())).toList();

                    Assertions.assertEquals(1, item.size());
                });
    }

    @Test
    @DisplayName("Adiciona matérias ao aluno e " +
            "verifica se elas foram realmente adicionadas")
    void getMateriasOfAluno() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turma = turmaCreatorMocks.createTurmaRandom(user);
        var turma2 = turmaCreatorMocks.createTurmaRandom(user);

        var professor = professorCreatorMocks.createRandomProfessor();

        var aluno1 = alunoCreatorMocks.createAlunoRandom(user, turma.getId());
        var aluno2 = alunoCreatorMocks.createAlunoRandom(user, turma.getId());
        var aluno3 = alunoCreatorMocks.createAlunoRandom(user, turma2.getId());

        var materia1 = materiaCreatorMocks
                .createRandomMateria(turma.getId(), professor.professorId());
        var materia2 = materiaCreatorMocks
                .createRandomMateria(turma.getId(), professor.professorId());

        var materia3 = materiaCreatorMocks
                .createRandomMateria(turma2.getId(), professor.professorId());

        var materiasOfTurma1 = List.of(materia1, materia2);

        var materiaGet = mockMvc.perform(MockMvcRequestBuilders
                .get("/materia/materias-of-aluno/{alunoId}", aluno1.idAluno()))
                .andReturn().getResponse();

        var materiaResult = materiaSamplesListJson.parseObject(materiaGet.getContentAsString());

        Assertions.assertEquals(materiasOfTurma1.size(), materiaResult.totalElements());

        materiaResult.content().forEach(m -> {
            var materiaFinded = materiasOfTurma1.stream().filter(mt ->
                    mt.materiaId().equals(m.idMateria())).toList();

            Assertions.assertEquals(1, materiaFinded.size());
        });
    }

    private void addPeriodoInMateria(UUID periodoId, UUID idMateria) throws Exception {
        mockMvc.
                perform(MockMvcRequestBuilders
                        .patch("/materia/add-periodo-in-materia/{materiaId}",
                                idMateria.toString())
                        .queryParam("periodoId", periodoId.toString()))
                .andReturn().getResponse();

    }
}
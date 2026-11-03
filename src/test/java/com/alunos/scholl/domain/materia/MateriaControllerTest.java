package com.alunos.scholl.domain.materia;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.AlunoSample;
import com.alunos.scholl.domain.creatorMocks.AlunoCreatorMocks;
import com.alunos.scholl.domain.creatorMocks.MateriaCreatorMocks;
import com.alunos.scholl.domain.creatorMocks.ProfessorCreatorMocks;
import com.alunos.scholl.domain.creatorMocks.TurmaCreatorMocks;
import com.alunos.scholl.domain.materia.dto.request.CreateMateriaRequest;
import com.alunos.scholl.domain.materia.dto.response.MateriaSample;
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
@WithMockUser(authorities = "DONO")
@Transactional
@Import({
        AlunoCreatorMocks.class,
        TurmaCreatorMocks.class,
        MateriaCreatorMocks.class,
        ProfessorCreatorMocks.class,
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
    private TurmaCreatorMocks turmaCreatorMocks;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

    @Autowired
    private JacksonTester<PageMock<MateriaSample>> materiaSamplesListJson;


    @Autowired
    private JacksonTester<MateriaSample> materiaSampleJacksonTester;

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

        var alunosInvalidos = List.of(materiaInvalid1, materiaInvalid2);
        var alunosValido = List.of(materiaValid1, materiaValid2, materiaValid3);

        var alunos = new ArrayList<>(alunosValido);

        alunos.addAll(alunosInvalidos);

        alunos.forEach(materiaCreatorMocks::createMateria);

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
    void changeTurmaInMateria() {
    }

    @Test
    @DisplayName("Adiciona nota aos alunos e verifica se " +
            "elas foram realmente adicionada")
    void getNotasOfMateria() {
    }

    @Test
    @DisplayName("Adiciona períodos na matéria e verifica " +
            "se eles foram realmente adicionados")
    void getPeriodosOfMateria() {
    }

    @Test
    @DisplayName("Envia um conjunto de materias válidas de um professor e verifica se" +
            "apenas a do professor foi retornada")
    void getMyMateriasByTeacher() {
    }

    @Test
    @DisplayName("Adiciona matérias ao aluno e " +
            "verifica se elas foram realmente adicionadas")
    void getMateriasOfAluno() {
    }

    @Test
    @DisplayName("Adiciona período na matéria e " +
            "verifica se foi realmente adicionada")
    void addPeriodoInMateria() {
    }
}
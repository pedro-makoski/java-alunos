package com.alunos.scholl.domain.aluno;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.PageMock;
import com.alunos.scholl.domain.aluno.dto.request.CreateAlunoRequest;
import com.alunos.scholl.domain.aluno.dto.response.AlunoSample;
import com.alunos.scholl.domain.aluno.dto.response.CreateAlunoResponse;
import com.alunos.scholl.domain.turma.model.TipoTurma;
import com.alunos.scholl.domain.turma.model.Turma;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private JacksonTester<CreateAlunoRequest> createAlunoRequest;

    @MockitoBean
    GetUserOnRequest getUserOnRequest;

    @Autowired
    private JacksonTester<CreateAlunoResponse> createAlunoResponseJacksonTester;

    @Autowired
    private JacksonTester<PageMock<AlunoSample>>  alunoSampleListJson;

    @Autowired
    private TurmaRepository turmaRepository;

    @Test
    @DisplayName("Validar se não aceita envios inválidos")
    @WithMockUser(authorities = "DONO")
    void createInvalid() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaRepository.save(Turma.builder()
                .dono(user)
                .build());

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

        users.forEach(this::getAlunoCreateResponse);
    }

    @Test
    @DisplayName("Validar se não aceita envios nulos")
    @WithMockUser(authorities = "DONO")
    void createInvalid2() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaRepository.save(Turma.builder()
                .dono(user)
                .build());

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

        users.forEach(this::getAlunoCreateResponse);
    }

    @Test
    @DisplayName("Validar se envia e o objeto está enviado")
    @WithMockUser(authorities = "DONO")
    void createValid() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaRepository.save(Turma.builder()
                .dono(user)
                .build());

        var userValid = new CreateAlunoRequest(
                turmaBase.getId(),
                "aaaaa",
                LocalDate.now().minusYears(30),
                "bbb",
                "ccc"
        );

        var res = getAlunoCreateResponse(userValid);

        Assertions.assertEquals(HttpStatus.CREATED.value(), res.getStatus());

    }


    @Test
    @DisplayName("Envia um conjunto de alunos válidos e inválidos e verifica se eles" +
            "se os válidos são retornados")
    @WithMockUser(authorities = "DONO")
    void getMyAlunos() throws Exception{
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaRepository.save(Turma.builder()
                .dono(user)
                .build());

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

        alunos.forEach(this::getAlunoCreateResponse);

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
    @WithMockUser(authorities = "DONO")
    void getMyAlunosByTeacher() throws Exception {
        var user = testUtils.addUserOnRequest();

        var turmaBase = turmaRepository.save(Turma.builder()
                .nome("azzz")
                .tipo(TipoTurma.FUNDAMENTAL)
                .tamanhoMaximo(30L)
                .dono(user)
                .build());

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

        var userDono = List.of(userValid1, userValid2);
        var usersTeacher = List.of(userValid1, userValid2, userValid3);

        var usersTeacherResponse =
                usersTeacher.stream().map(this::getAlunoCreateResponse).toList();

        var usersDonoResponse =
                userDono.stream().map(this::getAlunoCreateResponse).toList();
    }

    @Test
    @DisplayName("Envia um aluno válidos e espera retorno com os dados corretos")
    void getAlunoValidById() {
    }

    @Test
    @DisplayName("Envia um aluno válido de um professor " +
            "e espera retorno como encontrado ")
    void getAlunoValidByIdOnTeacher() {
    }

    @Test
    @DisplayName("Envia um aluno válido que não é do professor " +
            "e espera retorno como não encontrado ")
    void getAlunoValidByIdOnNotTeacher() {
    }

    @Test
    @DisplayName("Altera a turma do aluno e verifica a turma deles")
    void updateAlunoTurma() {
    }

    @Test
    @DisplayName("Envia um ID que não existe de um aluno e verifica se retorna erro")
    void updateAlunoTurmaInvalid() {
    }

    @Test
    @DisplayName("Envia um ID que não existe de uma matéria e verifica se retorna erro")
    void updateAlunoTurmaInvalid2() {
    }

    @Test
    @DisplayName("Cria aluno e adiciona notas com dados válidos e inválidos " +
            "e verifica se retorna corretamente")
    void getAlunoDetails() {
    }

    @Test
    @DisplayName("Adiciona nota ao aluno e verifica se está correto")
    void addNotaToAluno() {
    }

    @Test
    @DisplayName("Adiciona nota ao aluno e verifica se foi realmente adicionado")
    void getNotaAluno() {
    }

    public MockHttpServletResponse getAlunoCreateResponse(CreateAlunoRequest createAlunoRequestVar) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post("/aluno/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createAlunoRequest.write(createAlunoRequestVar).getJson()))
                    .andReturn().getResponse();
        } catch (Exception e) {
            fail("Erro ao enviar req: " + e.getMessage());
            return null;
        }
    }
}
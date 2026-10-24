package com.alunos.scholl.domain.aluno;

import com.alunos.scholl.TestUtils;
import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@WithMockUser
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    private JacksonTester<CreateAccountRequest> createAlunoRequest;

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
    @DisplayName("Envia um conjunto de alunos válidos e inválidos e verifica se eles" +
            "se os válidos são retornados")
    void getMyAlunos() {
    }

    @Test
    @DisplayName("Envia um aluno inválido e espera retorna not found")
    void getAlunoInvalidById() {
    }

    @Test
    @DisplayName("Envia um aluno válidos e espera retorno com os dados corretos")
    void getAlunoValidById() {
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
}
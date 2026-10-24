package com.alunos.scholl.domain.materia;

import com.alunos.scholl.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
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
class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

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
    @DisplayName("Envia um conjunto de matérias válidas e inválidas e verifica se os" +
            "dados válidos são retornados")
    void getMyMaterias() {
    }

    @Test
    @DisplayName("Envia uma matéria inválida e espera retorno not found")
    void getAlunoInvalidById() {
    }

    @Test
    @DisplayName("Envia um aluno válido e espera retorno com os dados corretos")
    void getAlunoValidById() {
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
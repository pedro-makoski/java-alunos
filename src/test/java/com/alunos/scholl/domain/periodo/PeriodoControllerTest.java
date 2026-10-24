package com.alunos.scholl.domain.periodo;

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
class PeriodoControllerTest {

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
}
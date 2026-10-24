package com.alunos.scholl.domain.user;

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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Test
    @DisplayName("Espera não permitir com logins incorretos")
    void loginIncorrect() {
    }

    @Test
    @DisplayName("Espera permitir com logins corretos")
    void loginCorrect() {
    }

    @Test
    @DisplayName("Cria contas inválidas e espera que não sejam adicionadas")
    void createAccountInvalid() {
    }

    @Test
    @DisplayName("Adiciona conta válida e espera encontrá-la")
    void createAccountValid() {
    }
}
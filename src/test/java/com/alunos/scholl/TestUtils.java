package com.alunos.scholl;

import com.alunos.scholl.domain.materia.dto.response.CreateMateriaResponse;
import com.alunos.scholl.domain.professor.dto.request.CreateProfessorRequest;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.domain.professor.repositories.ProfessorRepository;
import com.alunos.scholl.domain.professor.service.ProfessorService;
import com.alunos.scholl.domain.user.dtos.request.CreateAccountRequest;
import com.alunos.scholl.domain.user.model.User;
import com.alunos.scholl.domain.user.repositories.UserRepository;
import com.alunos.scholl.domain.user.service.GetUserOnRequest;
import com.alunos.scholl.domain.user.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Component
@TestConfiguration
public class TestUtils {

    public final int MIN_AGE = 20;
    public final int MAX_AGE = 50;

    @Autowired
    GetUserOnRequest getUserOnRequest;

    @Autowired
    LoginService loginService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UserRepository userRepository;

    public User addUserOnRequest() {
        var res = loginService.createAccount(new CreateAccountRequest(
                getRandomEmail(),
                UUID.randomUUID().toString(),
                getRandomDate(),
                getNome(),
                "Usuário Sobrenome"
        ));

        var user = userRepository.findById(res.getBody().idUser()).get();

        Mockito.when(getUserOnRequest.realizeWithThrow(
                Mockito.nullable(HttpServletRequest.class)))
                .thenReturn(user);

        return user;
    }

    public Professor addProfessorOnRequest(UUID professorId) {
        var professor = professorRepository.findById(professorId).get();

        Mockito.when(getUserOnRequest.realizeWithThrow(
                        Mockito.nullable(HttpServletRequest.class)))
                .thenReturn(professor.getUserRelated());


        return professor;
    }

    public LocalDate getRandomDate() {
        Random random = new Random();

        return LocalDate.now().minusYears(random.nextInt(MIN_AGE, MAX_AGE));
    }

    public String getNome() {
        return "Usuário Nome";
    }

    public String getRandomNome() {return UUID.randomUUID().toString();}

    public String getRandomEmail() {return UUID.randomUUID().toString()+"@email.com";}
}

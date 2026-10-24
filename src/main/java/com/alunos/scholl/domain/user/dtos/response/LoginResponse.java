package com.alunos.scholl.domain.user.dtos.response;

import java.util.UUID;

public record LoginResponse (
    String statusMsg,
    UUID idUser
){
}

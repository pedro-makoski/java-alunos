package com.alunos.scholl.infra.exceptionHandler;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ExceptionDto (
        String message
) { }

package com.alunos.scholl.infra.exceptionHandler;

import java.util.List;

public record InvalidExceptionDto(
        String message,
        List<FieldError> errors
) {
    public record FieldError(
            String campo,
            String message
    ) {

    }
}

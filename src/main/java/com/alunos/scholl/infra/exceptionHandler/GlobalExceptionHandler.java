package com.alunos.scholl.infra.exceptionHandler;

import com.alunos.scholl.infra.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<InvalidExceptionDto> methodArgumentNotValidException(
            ConstraintViolationException ex) {
        List<InvalidExceptionDto.FieldError> campos = ex
                .getConstraintViolations()
                .stream()
                .map(erro ->
                        new InvalidExceptionDto.FieldError(erro.getPropertyPath().toString(),
                                erro.getMessage()))
                .toList();

        return ResponseEntity
                .badRequest()
                .body(new InvalidExceptionDto(campos));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException() {
        return ResponseEntity
                .notFound()
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> mismatchBodyError(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                   "Formato do body incompátivel"
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> mismatchError(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                        "Tipo de parâmetros incorretos"
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> notFoundException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionDto(
                        "Houve um erro interno no sistema"
                ));
    }
}

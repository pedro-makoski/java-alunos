package com.alunos.scholl.infra.exceptionHandler;

import com.alunos.scholl.domain.user.exceptions.EmailAlreadyUsed;
import com.alunos.scholl.domain.user.exceptions.UserException;
import com.alunos.scholl.infra.exceptions.NotFoundException;
import com.alunos.scholl.infra.exceptions.YouDontHavePermission;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public final String badCredentialsMsg = "Usuário ou senha incorretos";


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


    @ExceptionHandler(YouDontHavePermission.class)
    public ResponseEntity<ExceptionDto> youDontHavePermission(YouDontHavePermission ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN.value())
                .body(new ExceptionDto(
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> mismatchBodyError(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                   "Formato do body incompátivel"
                ));
    }

    @ExceptionHandler(EmailAlreadyUsed.class)
    public ResponseEntity<ExceptionDto> errorWithEmail(EmailAlreadyUsed ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDto> badCredentials() {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                        badCredentialsMsg
                ));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionDto> badUserCredentials() {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                        badCredentialsMsg
                ));
    }



    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ExceptionDto> insuficientAuthenticationException() {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                        "Você está deslogado"
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
    public ResponseEntity<ExceptionDto> runtimeException(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body(new ExceptionDto(
                        "Houve um erro interno no sistema"
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionDto>
                campoCantBeNull(MissingServletRequestParameterException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(
                        String.format("O campo %s não pode ser nulo", ex.getParameterName())
                ));
    }
}

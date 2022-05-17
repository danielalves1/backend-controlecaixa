package com.omegasistemas.backendtest.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getLocalizedMessage();
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(value = { NoResultException.class })
  protected ResponseEntity<Object> handleNoResultException(RuntimeException ex, WebRequest request) {
    Map<String, Object> resp = new HashMap<>();
    resp.put("error", true);
    resp.put("message", "não foi possível realizar o acesso");
    resp.put("reason", "nenhum registro encontrado com as credenciais informadas");
    resp.put("detail", ex.getMessage());
    return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class, NumberFormatException.class })
  protected ResponseEntity<Object> handleArgumentTypeMismatchException(RuntimeException ex, WebRequest request) {
    Map<String, Object> resp = new HashMap<>();
    resp.put("error", true);
    resp.put("message", "não foi possível resolver a requisição");
    resp.put("reason", "o argumento passado na requisição está em um formato inválido, verifique");
    resp.put("detail", ex.getMessage());
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }

}

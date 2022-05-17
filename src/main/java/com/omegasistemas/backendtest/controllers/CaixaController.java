package com.omegasistemas.backendtest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.omegasistemas.backendtest.models.Caixa;
import com.omegasistemas.backendtest.repositories.CaixaRepository;
import com.omegasistemas.backendtest.validator.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/api")
public class CaixaController {
  
  @Autowired
  CaixaRepository caixaRepository;
  
  @Autowired
  Auth auth;
  
  // TODO: tratar corretamente a validação do token
  @ExceptionHandler(value = { NullPointerException.class })
  protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
    Map<String, Object> resp = new HashMap<>();
    resp.put("error", true);
    resp.put("message", "não é possível cadastrar o usuário");
    resp.put("reason", "você não tem permissão para realizar o procedimento");
    return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
  }

  /* GET ALL */
  @GetMapping("/caixa")
  public List<Caixa> getCaixa() {
    return caixaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
  }

  /* GET BY ID */
  @GetMapping("/caixa/{id}")
  public ResponseEntity<Object> getCaixaById(@PathVariable(value = "id") long id) {
    Caixa response = caixaRepository.findById((long) id);
    if (response == null || response.getId() < 1) {
      return new ResponseEntity<>(makeResponse(true, "registro não encontrado", null), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /* CREATE */
  @PostMapping("/caixa")
  public ResponseEntity<Object> createCaixa(@RequestBody Caixa caixa, WebRequest request) {
    if (auth.validateAccess(request)) {
      if (caixa.getDescricao() == null || caixa.getDescricao().length() < 3) {
        return new ResponseEntity<>(
            makeResponse(true, "não foi possível salvar o registro",
                "o campo 'descrição' deve ser preenchido com pelo menos 3 caracteres"),
            HttpStatus.BAD_REQUEST);
      }
      if (caixa.getSaldoinicial() == null) {
        caixa.setSaldoinicial(0.0);
      }
      Caixa response = caixaRepository.save(caixa);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível cadastrar o usuário");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  /* UPDATE */
  @PutMapping("/caixa/{id}")
  public ResponseEntity<Object> updateCaixa(@RequestBody Caixa caixa, @PathVariable(value = "id") long id,
      WebRequest request) {
    if (auth.validateAccess(request)) {
      if (caixa.getId() < 1 || caixa.getId() != id) {
        return new ResponseEntity<>(makeResponse(true, "não foi possível atualizar",
            "o ID do registro que você está tentando atualizar não corresponde ao ID que consta no Body da requisição"),
            HttpStatus.OK);
      }
      Caixa caixaToUpdate = caixaRepository.findById(id);
      if (caixaToUpdate.getId() > 0) {
        caixaToUpdate.setDescricao(caixa.getDescricao());
        caixaToUpdate.setSaldoinicial(caixa.getSaldoinicial());
        Caixa resp = caixaRepository.save(caixa);
        return new ResponseEntity<>(resp, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
            makeResponse(true, "não foi possível atualizar", "nenhum registro encontrado com o ID fornecido"),
            HttpStatus.NOT_FOUND);
      }
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível cadastrar o usuário");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  /* DELETE */
  @DeleteMapping("/caixa/{id}")
  public ResponseEntity<Object> deleteCaixa(@PathVariable(value = "id") long id, WebRequest request) {
    if (auth.validateAccess(request)) {
      try {
        caixaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception ex) {
        Map<String, Object> resp = makeResponse(true, "não foi possível deletar o registro", ex.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível cadastrar o usuário");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  /* MAKE SIMPLE MESSAGES TO RESPONSE */
  private Map<String, Object> makeResponse(Boolean error, String message, String reason) {
    Map<String, Object> resp = new HashMap<>();
    if (error) {
      resp.put("error", true);
      if (reason != null) {
        resp.put("reason", reason);
      }
    }
    resp.put("message", message);
    return resp;
  }

}

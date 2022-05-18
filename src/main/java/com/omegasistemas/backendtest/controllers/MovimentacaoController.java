package com.omegasistemas.backendtest.controllers;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.omegasistemas.backendtest.models.Balanco;
import com.omegasistemas.backendtest.models.Caixa;
import com.omegasistemas.backendtest.models.Movimentacao;
import com.omegasistemas.backendtest.repositories.CaixaRepository;
import com.omegasistemas.backendtest.repositories.CustomMovimentacaoRepository;
import com.omegasistemas.backendtest.repositories.MovimentacaoRepository;
import com.omegasistemas.backendtest.validator.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/api")
public class MovimentacaoController {

  @Autowired
  MovimentacaoRepository movimentacaoRepository;

  @Autowired
  CaixaRepository caixaRepository;

  @Autowired
  Auth auth;

  @Autowired
  CustomMovimentacaoRepository customMovimentacaoRepository;

  // TODO: tratar corretamente a validação do token
  @ExceptionHandler(value = { NullPointerException.class })
  protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
    Map<String, Object> resp = new HashMap<>();
    resp.put("error", true);
    resp.put("message", "ocorreu um erro");
    resp.put("reason", "você não tem permissão para realizar o procedimento");
    return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
  }

  /* GET ALL */
  @GetMapping("/movimentacao")
  public ResponseEntity<Object> getMovimentacao(@RequestParam(required = false) Long caixa,
      @RequestParam(required = false) Integer ano,
      @RequestParam(required = false) Integer mes,
      @RequestParam(required = false) Date data, WebRequest request) {
    if (auth.validateAccess(request)) {
      return new ResponseEntity<>(customMovimentacaoRepository.find(caixa, ano, mes, data), HttpStatus.OK);
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível listar os dados");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/movimentacao/anos")
  public ResponseEntity<Object> getAnos(WebRequest request) {
    if (auth.validateAccess(request)) {
      return new ResponseEntity<>(customMovimentacaoRepository.getAnos(), HttpStatus.OK);
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível listar os dados");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  /* CREATE */
  @PostMapping("/movimentacao")
  public ResponseEntity<Object> createMovimentacao(@RequestBody Movimentacao movimentacao, WebRequest request) {
    if (auth.validateAccess(request)) {
      if (movimentacao.getCaixa() != null && movimentacao.getCaixa().getId() > 0) {
        Caixa caixa = caixaRepository.findById(movimentacao.getCaixa().getId());
        if (caixa == null || caixa.getId() < 1) {
          return new ResponseEntity<>(
              makeResponse(true, "não foi possível salvar o registro",
                  "é necessário informar um caixa existente no banco de dados"),
              HttpStatus.BAD_REQUEST);
        }
        Movimentacao movimentacaoResponse = movimentacaoRepository.save(movimentacao);
        return new ResponseEntity<>(movimentacaoResponse, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
            makeResponse(true, "não foi possível salvar o registro",
                "o atributo 'caixa' deve estar devidamente preenchido no body da requisição"),
            HttpStatus.BAD_REQUEST);
      }
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível listar os dados");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  /* GET BALANCE */
  @GetMapping("/movimentacao/balanco")
  public ResponseEntity<Object> getBalanco(WebRequest request) {
    if (auth.validateAccess(request)) {
      Balanco balanco = (Balanco) customMovimentacaoRepository.getBalanco();
      return new ResponseEntity<>(balanco, HttpStatus.OK);
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("error", true);
      resp.put("message", "não é possível listar os dados");
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

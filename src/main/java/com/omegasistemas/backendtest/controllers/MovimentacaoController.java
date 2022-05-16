package com.omegasistemas.backendtest.controllers;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.omegasistemas.backendtest.models.Balanco;
import com.omegasistemas.backendtest.models.Caixa;
import com.omegasistemas.backendtest.models.Movimentacao;
import com.omegasistemas.backendtest.repositories.CaixaRepository;
import com.omegasistemas.backendtest.repositories.CustomMovimentacaoRepository;
import com.omegasistemas.backendtest.repositories.MovimentacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class MovimentacaoController {

  @Autowired
  MovimentacaoRepository movimentacaoRepository;

  @Autowired
  CaixaRepository caixaRepository;

  @Autowired
  CustomMovimentacaoRepository customMovimentacaoRepository;

  /* GET ALL */
  @GetMapping("/movimentacao")
  public List<Movimentacao> getMovimentacao(@RequestParam(required = false) Long caixa,
      @RequestParam(required = false) Integer ano,
      @RequestParam(required = false) Integer mes,
      @RequestParam(required = false) Date data) {
    return customMovimentacaoRepository.find(caixa, ano, mes, data);
  }

  /* CREATE */
  @PostMapping("/movimentacao")
  public ResponseEntity<Object> createMovimentacao(@RequestBody Movimentacao movimentacao) {
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
    } else
      return new ResponseEntity<>(
          makeResponse(true, "não foi possível salvar o registro",
              "o atributo 'caixa' deve estar devidamente preenchido no body da requisição"),
          HttpStatus.BAD_REQUEST);
  }

  /* GET BALANCE */
  @GetMapping("/movimentacao/balanco")
  public ResponseEntity<Object> getBalanco() {
    Balanco balanco = (Balanco) customMovimentacaoRepository.getBalanco();
    return new ResponseEntity<>(balanco, HttpStatus.OK);
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

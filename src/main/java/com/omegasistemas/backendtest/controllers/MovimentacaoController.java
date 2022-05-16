package com.omegasistemas.backendtest.controllers;

import java.util.List;

import com.omegasistemas.backendtest.models.Movimentacao;
import com.omegasistemas.backendtest.repositories.CustomMovimentacaoRepository;
import com.omegasistemas.backendtest.repositories.MovimentacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class MovimentacaoController {

  @Autowired
  MovimentacaoRepository movimentacaoRepository;

  @Autowired
  CustomMovimentacaoRepository customMovimentacaoRepository;

  /* GET ALL */
  @GetMapping("/movimentacao")
  public List<Movimentacao> getMovimentacao(@RequestParam(required = false) Long caixa,
      @RequestParam(required = false) String ano,
      @RequestParam(required = false) String mes,
      @RequestParam(required = false) String data) {
    return customMovimentacaoRepository.find(caixa, ano, mes, data);
    // return movimentacaoRepository.findAll();
  }

}

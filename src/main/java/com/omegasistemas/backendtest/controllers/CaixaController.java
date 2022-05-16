package com.omegasistemas.backendtest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.omegasistemas.backendtest.models.Caixa;
import com.omegasistemas.backendtest.repositories.CaixaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class CaixaController {

  @Autowired
  CaixaRepository caixaRepository;

  @GetMapping("/caixa")
  public List<Caixa> getCaixa() {
    return caixaRepository.findAll();
  }

  @GetMapping("/caixa/{id}")
  public ResponseEntity<Object> getCaixaById(@PathVariable(value = "id") long id) {
    Caixa response = caixaRepository.findById(id);
    if (response == null || response.getId() < 1) {
      return new ResponseEntity<>(makeResponse(true, "registro não encontrado", null), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/caixa")
  @ResponseBody
  public ResponseEntity<Object> createCaixa(@RequestBody Caixa caixa) {
    if (caixa.getDescricao() == null || caixa.getDescricao().length() < 3) {
      return new ResponseEntity<>(
          makeResponse(true, "não foi possível salvar o registro", "o campo 'descrição' é obrigatório"),
          HttpStatus.BAD_REQUEST);
    }
    if (caixa.getSaldoinicial() == null) {
      caixa.setSaldoinicial(0.0);
    }
    Caixa response = caixaRepository.save(caixa);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/caixa/{id}")
  public ResponseEntity<Object> deleteCaixa(@PathVariable(value = "id") long id) {
    try {
      caixaRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception ex) {
      Map<String, Object> resp = makeResponse(true, "não foi possível deletar o registro", ex.getMessage());
      return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

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

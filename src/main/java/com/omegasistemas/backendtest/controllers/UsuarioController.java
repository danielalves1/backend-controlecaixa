package com.omegasistemas.backendtest.controllers;

import java.util.HashMap;
import java.util.Map;

import com.omegasistemas.backendtest.models.Usuario;
import com.omegasistemas.backendtest.repositories.CustomUsuarioRepository;
import com.omegasistemas.backendtest.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class UsuarioController {

  @Autowired
  UsuarioRepository usuarioRepository;

  @Autowired
  CustomUsuarioRepository customUsuarioRepository;

  @PostMapping("/usuario")
  public ResponseEntity<Object> createUsuario(@RequestBody Usuario usuario) {
    Usuario created = usuarioRepository.save(usuario);
    return new ResponseEntity<>(created, HttpStatus.OK);
  }

  @PostMapping("/usuario/login")
  public ResponseEntity<Object> login(@RequestBody Usuario usuario) {
    Usuario logged = customUsuarioRepository.attempt(usuario);
    if (logged != null) {
      Map<String, Object> resp = logged.serialized();
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } else {
      Map<String, Object> response = new HashMap<>();
      response.put("error", true);
      response.put("message", "não foi possível realizar o acesso");
      response.put("reason", "usuário e/ou senha não correspondem");
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
  }

}

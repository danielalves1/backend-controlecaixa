package com.omegasistemas.backendtest.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.omegasistemas.backendtest.models.Usuario;
import com.omegasistemas.backendtest.repositories.CustomUsuarioRepository;
import com.omegasistemas.backendtest.repositories.UsuarioRepository;
import com.omegasistemas.backendtest.validator.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/api")
public class UsuarioController {

  private final String secret = "teste-backend-secret";

  @Autowired
  UsuarioRepository usuarioRepository;

  @Autowired
  CustomUsuarioRepository customUsuarioRepository;

  @Autowired
  Auth auth;

  @PostMapping("/usuario")
  public ResponseEntity<Object> createUsuario(@RequestBody Usuario usuario, WebRequest request) {
    boolean authorized = auth.validateAccess(request);
    Map<String, Object> resp = new HashMap<>();
    if (authorized) {
      Usuario created = usuarioRepository.save(usuario);
      return new ResponseEntity<>(created, HttpStatus.OK);
    } else {
      resp.put("error", true);
      resp.put("message", "não é possível cadastrar o usuário");
      resp.put("reason", "você não tem permissão para realizar o procedimento");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/usuario/login")
  public ResponseEntity<Object> login(@RequestBody Usuario usuario) {
    Usuario logged = customUsuarioRepository.attempt(usuario);
    if (logged != null) {
      Map<String, Object> resp = logged.serialized();
      Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
      String token = JWT.create().withSubject(resp.get("usuario").toString())
          .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
          .withClaim("nivel_acesso", resp.get("nivel_acesso").toString())
          .sign(algorithm);
      resp.put("token", token);
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

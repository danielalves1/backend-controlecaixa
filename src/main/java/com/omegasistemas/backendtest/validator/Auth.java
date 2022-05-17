package com.omegasistemas.backendtest.validator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.WebRequest;

@Repository
public class Auth {

  private final String secret = "teste-backend-secret";

  public boolean validateAccess(WebRequest request) {
    String token = request.getHeader("Authorization");
    token = token.split(" ")[1];
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT decodedJWT = verifier.verify(token);
      Integer nivelAcesso = Integer.parseInt(decodedJWT.getClaim("nivel_acesso").asString());
      return nivelAcesso < 2;
    } catch (Exception ex) {
      return false;
    }
  }
}

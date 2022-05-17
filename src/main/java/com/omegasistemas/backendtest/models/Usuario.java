package com.omegasistemas.backendtest.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private String usuario;

  @Column(nullable = false)
  private String senha;

  @Column(nullable = false)
  private Integer nivel_acesso = 1;

  @Column(nullable = false)
  private Boolean ativo = true;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = DigestUtils.sha256Hex(senha);
    System.out.println("Senha gerada");
    System.out.println(this.senha);
  }

  public Integer getNivel_acesso() {
    return nivel_acesso;
  }

  public void setNivel_acesso(Integer nivel_acesso) {
    this.nivel_acesso = nivel_acesso;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public Map<String, Object> serialized() {
    Map<String, Object> serialized = new HashMap<>();
    serialized.put("nome", this.nome);
    serialized.put("usuario", this.usuario);
    serialized.put("ativo", this.ativo);
    serialized.put("nivel_acesso", this.nivel_acesso);
    return serialized;
  }

}

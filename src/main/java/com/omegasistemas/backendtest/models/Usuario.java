package com.omegasistemas.backendtest.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private String usuario;

  @Column(nullable = false)
  private String senha;

  @Column(nullable = false, columnDefinition = "0 - Acesso Total | 1 - Apenas movimentacao")
  private Integer nivel_acesso = 1;

  @Column(nullable = false)
  private Boolean ativo = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    this.senha = senha;
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

  

}

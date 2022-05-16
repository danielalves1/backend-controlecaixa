package com.omegasistemas.backendtest.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Caixa {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String descricao;
  private Double saldoinicial;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Double getSaldoinicial() {
    return saldoinicial;
  }

  public void setSaldoinicial(Double saldoinicial) {
    this.saldoinicial = saldoinicial;
  }

}

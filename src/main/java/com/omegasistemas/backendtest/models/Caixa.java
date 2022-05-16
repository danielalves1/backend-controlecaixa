package com.omegasistemas.backendtest.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Caixa {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String descricao;
  private Double saldoinicial;

  @OneToMany
  private List<Movimentacao> movimentacoes;

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

  public List<Movimentacao> getMovimentacoes() {
    return movimentacoes;
  }

  public void setMovimentacoes(List<Movimentacao> movimentacoes) {
    this.movimentacoes = movimentacoes;
  }
}

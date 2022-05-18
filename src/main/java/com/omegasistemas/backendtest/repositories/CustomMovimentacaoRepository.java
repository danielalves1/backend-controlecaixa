package com.omegasistemas.backendtest.repositories;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.omegasistemas.backendtest.models.Balanco;
import com.omegasistemas.backendtest.models.Movimentacao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMovimentacaoRepository {
  private final EntityManager em;

  public CustomMovimentacaoRepository(EntityManager em) {
    this.em = em;
  }

  public Balanco getBalanco() {
    String sql = "select sum(case when M.tipo = 'E' then M.valor else 0 end) as entradas, sum(case when M.tipo = 'S' then M.valor else 0 end) as saidas, 0.0 as balanco from Movimentacao M";
    var query = em.createNativeQuery(sql);
    Object[] result = (Object[]) query.getSingleResult();
    Balanco balanco = new Balanco();
    balanco.setEntradas((Double) result[0]);
    balanco.setSaidas((Double) result[1]);
    balanco.setBalanco(balanco.getEntradas() - balanco.getSaidas());
    return balanco;
  }

  public List<Integer> getAnos() {
    String sql = "select DISTINCT EXTRACT(YEAR from M.data) as ano from Movimentacao M";
    var query = em.createQuery(sql, Integer.class);
    var result = query.getResultList();
    return result;
  }

  public List<Movimentacao> find(Long id, Integer ano, Integer mes, Date data) {
    String query = "select M from Movimentacao M join M.caixa C where 1=1 ";

    if (id != null && id > 0) {
      query += "and C.id = :id ";
    }
    if (ano != null) {
      query += "and extract(YEAR from M.data) = :ano ";
    }
    if (mes != null) {
      query += "and extract(MONTH from M.data) = :mes ";
    }
    if (data != null) {
      query += "and M.data = :data ";
    }

    var completeQuery = em.createQuery(query, Movimentacao.class);

    if (id != null && id > 0) {
      completeQuery.setParameter("id", id);
    }
    if (ano != null) {
      completeQuery.setParameter("ano", ano);
    }
    if (mes != null) {
      completeQuery.setParameter("mes", mes);
    }
    if (data != null) {
      completeQuery.setParameter("data", data);
    }

    return completeQuery.getResultList();
  }
}

package com.omegasistemas.backendtest.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import com.omegasistemas.backendtest.models.Movimentacao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMovimentacaoRepository {
  private final EntityManager em;

  public CustomMovimentacaoRepository(EntityManager em) {
    this.em = em;
  }

  public List<Movimentacao> find(long id, String ano, String mes, String data) {
    String query = "select M from Movimentacao M join M.caixa C where 1=1 ";

    if (id > 0) {
      query += "and C.id = :id ";
    }
    if (ano != null) {
      query += "and extract(YEAR from M.data) = :ano ";
    }
    if (mes != null) {
      query += "and extract(MONTH from M.data) = :mes ";
    }
    if (ano != null) {
      query += "and M.data = :data ";
    }

    var completeQuery = em.createQuery(query, Movimentacao.class);

    if (id > 0) {
      completeQuery.setParameter("id", id);
    }
    if (ano != null) {
      completeQuery.setParameter("ano", ano);
    }
    if (mes != null) {
      completeQuery.setParameter("mes", mes);
    }
    if (ano != null) {
      completeQuery.setParameter("data", data);
    }

    return completeQuery.getResultList();
  }
}

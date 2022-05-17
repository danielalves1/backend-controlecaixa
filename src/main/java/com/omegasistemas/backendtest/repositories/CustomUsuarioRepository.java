package com.omegasistemas.backendtest.repositories;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.omegasistemas.backendtest.models.Usuario;

import org.springframework.stereotype.Repository;

@Repository
public class CustomUsuarioRepository {
  private final EntityManager em;

  public CustomUsuarioRepository(EntityManager em) {
    this.em = em;
  }

  public Usuario attempt(Usuario usuario) {
    String sql = "select U from Usuario U where U.usuario = :usuario and U.senha = :senha";
    TypedQuery<Usuario> query = em.createQuery(sql, Usuario.class);
    query.setParameter("usuario", usuario.getUsuario());
    query.setParameter("senha", usuario.getSenha());
    Usuario logged = query.getSingleResult();
    if (logged != null && logged.getId() > 0) {
      return logged;
    }
    return null;
  }
}

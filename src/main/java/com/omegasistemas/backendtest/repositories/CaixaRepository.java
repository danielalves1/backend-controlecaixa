package com.omegasistemas.backendtest.repositories;

import com.omegasistemas.backendtest.models.Caixa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

  Caixa findById(long id);

}

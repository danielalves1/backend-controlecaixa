package com.omegasistemas.backendtest.repositories;

import com.omegasistemas.backendtest.models.Movimentacao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

}

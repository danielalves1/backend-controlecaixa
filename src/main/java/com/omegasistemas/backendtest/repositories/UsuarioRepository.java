package com.omegasistemas.backendtest.repositories;

import com.omegasistemas.backendtest.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}

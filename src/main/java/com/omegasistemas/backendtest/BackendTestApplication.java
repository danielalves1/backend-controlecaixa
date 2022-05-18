package com.omegasistemas.backendtest;

import com.omegasistemas.backendtest.models.Usuario;
import com.omegasistemas.backendtest.repositories.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendTestApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendTestApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository) {
		return args -> {
			Usuario usuario = new Usuario();
			usuario.setAtivo(true);
			usuario.setNivel_acesso(0);
			usuario.setNome("Daniel Alves");
			usuario.setUsuario("daniel");
			usuario.setSenha("123");
			usuarioRepository.save(usuario);
		};
	}

}

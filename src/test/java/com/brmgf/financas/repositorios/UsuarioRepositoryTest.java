package com.brmgf.financas.repositorios;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.brmgf.financas.modelo.Usuario;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;

	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		Usuario usuario = Usuario.builder().nome("Teste").email("email@email.com.br").build();
		repository.save(usuario);
		
		boolean existeUsuario = repository.existsByEmail("email@email.com.br");
		Assertions.assertThat(existeUsuario).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoEmailNaoCadastrado() {
		repository.deleteAll();
		
		boolean existeUsuario = repository.existsByEmail("email@email.com.br");
		Assertions.assertThat(existeUsuario).isFalse();
	}
}

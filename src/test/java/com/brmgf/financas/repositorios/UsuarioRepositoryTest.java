package com.brmgf.financas.repositorios;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brmgf.financas.modelo.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	public static Usuario criaUsuario() {
		return Usuario.builder()
				.nome("test")
				.email("email@email.com.br")
				.senha("123")
				.build();
	}

	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		Usuario usuario = criaUsuario();
		entityManager.persist(usuario);
		
		boolean existeUsuario = repository.existsByEmail("email@email.com.br");
		Assertions.assertThat(existeUsuario).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoEmailNaoCadastrado() {
		boolean existeUsuario = repository.existsByEmail("email@email.com.br");
		Assertions.assertThat(existeUsuario).isFalse();
	}
	
	@Test
	public void devePersistirUsuarioNaBaseDeDados() {
		Usuario usuario = criaUsuario();
		Usuario usuarioSalvo = repository.save(usuario);
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveRetornarVerdadeiroAoBuscarUmUsuarioPorEmailCadastrado() {
		Usuario usuario = criaUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> usuarioOptional = repository.findByEmail(usuario.getEmail());
		Assertions.assertThat(usuarioOptional.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoAoBuscaUmUsuarioPorEmailNaoCadastrado() {		
		Optional<Usuario> usuarioOptional = repository.findByEmail("teste@email.com.br");
		Assertions.assertThat(usuarioOptional.isPresent()).isFalse();
	}

}

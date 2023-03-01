package com.brmgf.financas.servico;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brmgf.financas.exceptions.CadastroException;
import com.brmgf.financas.modelo.Usuario;
import com.brmgf.financas.repositorios.UsuarioRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Profile("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveValidarEmailComSucesso() {
		repository.deleteAll();
		service.validarEmail("email@email.com.br");
	}
	
	@Test
	public void deveLancarErroAoValidarEmailJaCadastrado() {
		
		assertThrows(CadastroException.class, () -> { 
			Usuario usuario = Usuario.builder().id(anyLong()).email("email@email.com.br").build();
			repository.save(usuario);
			service.validarEmail(usuario.getEmail());
			}
		);
		
	}
}

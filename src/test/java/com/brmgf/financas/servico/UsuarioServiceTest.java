package com.brmgf.financas.servico;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brmgf.financas.exceptions.CadastroException;
import com.brmgf.financas.repositorios.UsuarioRepository;
import com.brmgf.financas.servico.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@BeforeAll
	public void setUp() {
		service = new UsuarioServiceImpl(repository);
	}
	
	@Test
	public void deveValidarEmailNaoCadastrado() {
		when(repository.existsByEmail(anyString())).thenReturn(false);
		service.validarEmail(anyString());
	}
	
	@Test
	public void deveLancarErroAoValidarEmailJaCadastrado() {	
		when(repository.existsByEmail(anyString())).thenReturn(true);
		assertThrows(CadastroException.class, () -> { 
			service.validarEmail(anyString());
			}
		);		
	}
}

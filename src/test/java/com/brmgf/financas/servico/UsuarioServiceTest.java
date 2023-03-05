package com.brmgf.financas.servico;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brmgf.financas.exceptions.AutenticacaoException;
import com.brmgf.financas.exceptions.CadastroException;
import com.brmgf.financas.modelo.Usuario;
import com.brmgf.financas.repositorios.UsuarioRepository;
import com.brmgf.financas.servico.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	
	@Test
	public void deveValidarEmailNaoCadastrado() {
		when(repository.existsByEmail(anyString())).thenReturn(false);
		service.validarEmail(anyString());
	}
	
	@Test
	public void deveLancarErroAoValidarEmailJaCadastrado() {	
		when(repository.existsByEmail(anyString())).thenReturn(true);
		
		Throwable exception = Assertions.catchException(() -> service.validarEmail(anyString()));
		Assertions.assertThat(exception).isInstanceOf(CadastroException.class).hasMessage("Já existe um usuário cadastrado com esse e-mail");		
	}
	
	@Test
	public void deveAutenticarUsuario() {
		String email = "email@email.com";
		String senha = "123";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario resultado = service.autenticar(email, senha);	
		Assertions.assertThat(resultado).isNotNull();
	}
	
	@Test
	public void deveLancarErroAoAutenticarUsuarioNaoCadastrado() {
		when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
		
		Throwable exception = Assertions.catchException(() -> service.autenticar("email@email.com.br", "123"));
		Assertions.assertThat(exception).isInstanceOfAny(AutenticacaoException.class).hasMessage("Usuario não encontrado");
	}
	
	@Test
	public void deveLancarErroAoAutenticarSenhaErrada() {
		String email = "email@email.com";
		String senha = "123";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "1234"));
		Assertions.assertThat(exception).isInstanceOfAny(AutenticacaoException.class).hasMessage("Senha inválida");	
	}
	
	@Test
	public void deveSalvarUsuario() {
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("Maria")
				.email("maria@email.com.br")
				.senha("123")
				.build();
		
		doNothing().when(service).validarEmail(anyString());
		when(repository.save(any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = service.salvar(new Usuario());
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Maria");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("maria@email.com.br");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("123");
	}
	
	@Test
	public void deveLancarErroAoTentarSalvarUsuarioJaCadastrado() {
		String email = "email@email.com.br";
		Usuario usuario = Usuario.builder().email(email).build();
		
		doThrow(CadastroException.class).when(service).validarEmail(email);
		
		verify(repository, never()).save(usuario);
	}
	
}

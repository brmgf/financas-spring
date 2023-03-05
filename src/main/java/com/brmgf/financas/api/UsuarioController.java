package com.brmgf.financas.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brmgf.financas.dto.UsuarioDto;
import com.brmgf.financas.exceptions.AutenticacaoException;
import com.brmgf.financas.exceptions.CadastroException;
import com.brmgf.financas.modelo.Usuario;
import com.brmgf.financas.servico.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private UsuarioService service;
	
	public UsuarioController(UsuarioService service) {
		this.service = service;
	}

	@PostMapping("/novo")
	public ResponseEntity salvar(@RequestBody UsuarioDto dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			Usuario usuarioSalvo = service.salvar(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (CadastroException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDto dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (AutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}

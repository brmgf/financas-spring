package com.brmgf.financas.servico.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brmgf.financas.exceptions.AutenticacaoException;
import com.brmgf.financas.exceptions.CadastroException;
import com.brmgf.financas.modelo.Usuario;
import com.brmgf.financas.repositorios.UsuarioRepository;
import com.brmgf.financas.servico.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuarioOptional = repository.findByEmail(email);
		
		if(!usuarioOptional.isPresent())
			throw new AutenticacaoException("Usuario não encontrado.");
		
		if(!usuarioOptional.get().getSenha().equals(senha))
			throw new AutenticacaoException("Credenciais inválidas.");
		
		return usuarioOptional.get();
	}

	@Override
	@Transactional
	public Usuario salvar(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existeEmail = repository.existsByEmail(email);
		if(existeEmail) {
			throw new CadastroException("Já existe um usuário cadastrado com esse e-mail");
		}
	}

}

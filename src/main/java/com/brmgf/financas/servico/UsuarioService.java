package com.brmgf.financas.servico;

import java.util.Optional;

import com.brmgf.financas.modelo.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvar(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> buscaUsuarioPorId(Long id);
	
}

package com.brmgf.financas.servico;

import com.brmgf.financas.modelo.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvar(Usuario usuario);
	
	void validarEmail(String email);
	
	
}

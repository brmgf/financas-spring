package com.brmgf.financas.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brmgf.financas.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);

}

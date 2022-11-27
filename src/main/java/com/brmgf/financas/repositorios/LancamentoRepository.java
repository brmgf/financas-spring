package com.brmgf.financas.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brmgf.financas.modelo.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}

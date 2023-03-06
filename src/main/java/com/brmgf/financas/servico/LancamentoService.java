package com.brmgf.financas.servico;

import java.util.List;
import java.util.Optional;

import com.brmgf.financas.enums.StatusLancamento;
import com.brmgf.financas.modelo.Lancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void excluir(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamento);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validarLancamento(Lancamento lancamento);
	
	Optional<Lancamento> buscaLancamentoPorId(Long id);
	
}

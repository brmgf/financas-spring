package com.brmgf.financas.servico.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brmgf.financas.enums.StatusLancamento;
import com.brmgf.financas.enums.TipoLancamento;
import com.brmgf.financas.exceptions.LancamentoException;
import com.brmgf.financas.modelo.Lancamento;
import com.brmgf.financas.repositorios.LancamentoRepository;
import com.brmgf.financas.servico.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validarLancamento(lancamento);
		lancamento.setStatusLancamento(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validarLancamento(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void excluir(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamento) {
		Example<Lancamento> example = Example.of(lancamento, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatusLancamento(status);
		atualizar(lancamento);
	}

	@Override
	public void validarLancamento(Lancamento lancamento) {
		if(Objects.isNull(lancamento.getDescricao()) || lancamento.getDescricao().trim().equals("")) 
			throw new LancamentoException("Informe uma descrição para lançamento");
		
		if(Objects.isNull(lancamento.getMes()) || lancamento.getMes() < 1 || lancamento.getMes() > 12)
			throw new LancamentoException("Informe um mês válido");
		
		if(Objects.isNull(lancamento.getAno()) || lancamento.getAno().toString().length() != 4)
			throw new LancamentoException("Informe um ano válido");
		
		if(Objects.isNull(lancamento.getUsuario()) || Objects.isNull(lancamento.getUsuario().getId()))
			throw new LancamentoException("Informe um usuário para lançamento");
		
		if(Objects.isNull(lancamento.getValor()) || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1)
			throw new LancamentoException("Informe um valor válido para lançamento");
		
		if(Objects.isNull(lancamento.getTipoLancamento()))
			throw new LancamentoException("Informe um tipo para lançamento");
	}

	@Override
	public Optional<Lancamento> buscaLancamentoPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if(Objects.isNull(receitas))
			receitas = BigDecimal.ZERO;
		if(Objects.isNull(despesas))
			receitas = BigDecimal.ZERO;
		
		return receitas.subtract(despesas);
	}

}

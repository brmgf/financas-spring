package com.brmgf.financas.repositorios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.brmgf.financas.enums.StatusLancamento;
import com.brmgf.financas.enums.TipoLancamento;
import com.brmgf.financas.modelo.Lancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarLancamento() {
		Lancamento lancamento = criaLancamento();
		
		lancamento = repository.save(lancamento);
		assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveExcluirLancamento() {
		Lancamento lancamento = persisteLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarLancamento() {
		Lancamento lancamento = persisteLancamento();
		lancamento.setAno(2022);
		lancamento.setMes(3);
		lancamento.setDescricao("Lancamento 2022");
		lancamento.setStatusLancamento(StatusLancamento.CANCELADO);
		lancamento.setValor(BigDecimal.valueOf(500.30));
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2022);
		assertThat(lancamentoAtualizado.getMes()).isEqualTo(3);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Lancamento 2022");
		assertThat(lancamentoAtualizado.getStatusLancamento()).isEqualTo(StatusLancamento.CANCELADO);
		assertThat(lancamentoAtualizado.getValor()).isEqualTo(BigDecimal.valueOf(500.30));
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criaLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	private Lancamento criaLancamento() {
		return Lancamento.builder()
				.ano(2023)
				.mes(1)
				.descricao("Lancamento")
				.valor(BigDecimal.valueOf(100))
				.tipoLancamento(TipoLancamento.RECEITA)
				.statusLancamento(StatusLancamento.EFETIVADO)
				.dataCadastro(LocalDate.now()).build();
	}
	
	private Lancamento persisteLancamento() {
		Lancamento lancamento = criaLancamento();
		return entityManager.persist(lancamento);
	}

}

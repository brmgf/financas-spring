package com.brmgf.financas.repositorios;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brmgf.financas.enums.TipoLancamento;
import com.brmgf.financas.modelo.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query(value = "select sum(l.valor) from Lancamento l join l.usuario u where u.id = :idUsuario "
			+ "and l.tipoLancamento = :tipo group by u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario")Long idUsuario, @Param("tipo") TipoLancamento tipo);

}

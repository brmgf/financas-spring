package com.brmgf.financas.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brmgf.financas.dto.AtualizaStatusDto;
import com.brmgf.financas.dto.LancamentoDto;
import com.brmgf.financas.enums.StatusLancamento;
import com.brmgf.financas.exceptions.CadastroException;
import com.brmgf.financas.exceptions.LancamentoException;
import com.brmgf.financas.modelo.Lancamento;
import com.brmgf.financas.enums.TipoLancamento;
import com.brmgf.financas.modelo.Usuario;
import com.brmgf.financas.servico.LancamentoService;
import com.brmgf.financas.servico.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentoService service;
	private final UsuarioService usuarioService;

	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody LancamentoDto dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (LancamentoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto dto) {
		return service.buscaLancamentoPorId(id).map(entidadeLancamento -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entidadeLancamento.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch(LancamentoException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		return service.buscaLancamentoPorId(id).map(entidadeLancamento -> {
			service.excluir(entidadeLancamento);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuarioOptional = usuarioService.buscaUsuarioPorId(idUsuario);
		if(usuarioOptional.isEmpty())
			return ResponseEntity.badRequest().body("Usuário não encontrado");
		
		lancamentoFiltro.setUsuario(usuarioOptional.get());
		List<Lancamento> lancamentosFiltrados = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentosFiltrados);
	}
	
	@PutMapping("{id}/atualizar-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDto dto) {
		return service.buscaLancamentoPorId(id).map(entidadeLancamento -> {
			StatusLancamento status = StatusLancamento.valueOf(dto.getStatus());
			if(status == null)
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lancamento");
			try {
				entidadeLancamento.setStatusLancamento(status);
				service.atualizar(entidadeLancamento);
				return ResponseEntity.ok(entidadeLancamento);
			} catch(LancamentoException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	private Lancamento converter(LancamentoDto dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setMes(dto.getMes());
		lancamento.setAno(dto.getAno());
		lancamento.setValor(dto.getValor());
		lancamento.setTipoLancamento(Objects.nonNull(dto.getTipoLancamento()) ? 
				TipoLancamento.valueOf(dto.getTipoLancamento()) : null);
		lancamento.setStatusLancamento(Objects.nonNull(dto.getStatusLancamento()) ? 
				StatusLancamento.valueOf(dto.getStatusLancamento()) : StatusLancamento.PENDENTE);
		
		Usuario usuario = usuarioService.buscaUsuarioPorId(dto.getUsuario())
				.orElseThrow(() -> new CadastroException("Usuário não encontrado"));
		lancamento.setUsuario(usuario);
		return lancamento;
	}

}

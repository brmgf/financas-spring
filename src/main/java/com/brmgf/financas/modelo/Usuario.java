package com.brmgf.financas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario", schema = "financas")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "email")
	private String email;
	@Column(name = "senha")
	private String senha;

}

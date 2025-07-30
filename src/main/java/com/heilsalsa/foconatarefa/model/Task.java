package com.heilsalsa.foconatarefa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity //Marcando que a claase vira uma tabla no banco
public class Task {
	
	@Id //chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremendo do postgres

	private Long id;
	private String titulo;
	private String descricao;
	private Boolean feito = false; // Pendente por padrão
	private LocalDateTime dataCriacao = LocalDateTime.now();
	private LocalDateTime horarioLimite;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Boolean getFeito() {
		return feito;
	}
	public void setFeito(Boolean feito) {
		this.feito = feito;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public LocalDateTime getHorarioLimite() {
		return horarioLimite;
	}
	public void setHorarioLimite(LocalDateTime horarioLimite) {
		this.horarioLimite = horarioLimite;
	} 
	
	
	
	
	
	
}

package com.heilsalsa.foconatarefa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;


@Entity //Marcando que a claase vira uma tabla no banco
@Table(name = "tasks")
public class Task {
	
	@Id //chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremendo do postgres
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@NotBlank(message = "O Título é obrigatório")
	@Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
	private String titulo;
	
	@Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
	private String descricao;
	@NotNull(message = "O Status 'feito' não pode ser nulo")
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

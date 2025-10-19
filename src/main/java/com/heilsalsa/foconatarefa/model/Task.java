package com.heilsalsa.foconatarefa.model;

import java.time.LocalDateTime;

import com.heilsalsa.foconatarefa.domain.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // dono da tarefa (opcional manter agora, mas já preparado)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "O Título é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String titulo;

    @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
    private String descricao;

    // Status + datas de controle
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.ACTIVE;

    private LocalDateTime completedAt;
    private LocalDateTime deletedAt;

    // metadados
    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime horarioLimite;
    

    // ---------- lifecycle ----------
    @PrePersist
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }

    // ---------- getters/setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getHorarioLimite() { return horarioLimite; }
    public void setHorarioLimite(LocalDateTime horarioLimite) { this.horarioLimite = horarioLimite; }
}

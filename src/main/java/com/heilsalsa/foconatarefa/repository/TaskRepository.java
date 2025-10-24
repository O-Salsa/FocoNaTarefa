package com.heilsalsa.foconatarefa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heilsalsa.foconatarefa.domain.TaskStatus;
import com.heilsalsa.foconatarefa.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Pendentes (não deletadas)
    Page<Task> findByDeletedAtIsNullAndStatus(TaskStatus status, Pageable pageable);

    // Concluídas por intervalo de datas
    Page<Task> findByDeletedAtIsNullAndStatusAndCompletedAtBetween(
        TaskStatus status,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    );

    // Caso precise “todas não deletadas”
    Page<Task> findByDeletedAtIsNull(Pageable pageable);
    
    // Itens enviados para a lixeira
    Page<Task> findByDeletedAtIsNotNull(Pageable pageable);
    
    Page<Task> findByStatus(TaskStatus status, Pageable pageable); // p. ex. status=DELETED

}

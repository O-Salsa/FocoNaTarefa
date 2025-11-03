package com.heilsalsa.foconatarefa.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heilsalsa.foconatarefa.domain.TaskStatus;
import com.heilsalsa.foconatarefa.exceptions.TaskNotFoundException;
import com.heilsalsa.foconatarefa.model.Task;
import com.heilsalsa.foconatarefa.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public Page<Task> findActive(TaskStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        if (status == TaskStatus.COMPLETED && from != null && to != null) {
            return repo.findByDeletedAtIsNullAndStatusAndCompletedAtBetween(status, from, to, pageable);
        }
        return repo.findByDeletedAtIsNullAndStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Task> findDeleted(Pageable pageable) {
        return repo.findByDeletedAtIsNotNull(pageable);
    }

    @Transactional
    public Task create(Task task) {
        return repo.save(task);
    }

    @Transactional
    public Task complete(Long id) {
        Task t = getTaskOrThrow(id);
        if (t.getDeletedAt() != null)
            throw new IllegalStateException("Não é possível concluir uma tarefa na Lixeira");
        t.setStatus(TaskStatus.COMPLETED);
        t.setCompletedAt(LocalDateTime.now());
        return t;
    }

    @Transactional
    public Task uncomplete(Long id) {
        Task t = getTaskOrThrow(id);
        if (t.getDeletedAt() != null)
            throw new IllegalStateException("Não é possível reabrir uma tarefa na Lixeira");
        t.setStatus(TaskStatus.ACTIVE);
        t.setCompletedAt(null);
        return t;
    }

    @Transactional
    public Task softDelete(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new RuntimeException("Task não encontrada"));
        t.setStatus(TaskStatus.DELETED);
        t.setDeletedAt(LocalDateTime.now());
        return t;
    }

    @Transactional
    public Task restore(Long id) {
        Task t = getTaskOrThrow(id);
        t.setStatus(TaskStatus.ACTIVE);
        t.setDeletedAt(null);
        if (t.getStatus() == TaskStatus.DELETED) {
            t.setStatus(TaskStatus.ACTIVE);
        }
        return t;
    }

    @Transactional
    public void purge(Long id) {
        Task t = getTaskOrThrow(id);
        repo.delete(t);
    }

    private Task getTaskOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}
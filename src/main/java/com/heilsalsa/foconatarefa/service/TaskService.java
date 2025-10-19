package com.heilsalsa.foconatarefa.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.heilsalsa.foconatarefa.domain.TaskStatus;
import com.heilsalsa.foconatarefa.model.Task;
import com.heilsalsa.foconatarefa.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Task complete(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new RuntimeException("Task não encontrada"));
        if (t.getStatus() == TaskStatus.DELETED)
            throw new IllegalStateException("Não é possível concluir uma tarefa na Lixeira");
        t.setStatus(TaskStatus.COMPLETED);
        t.setCompletedAt(LocalDateTime.now());
        return repo.save(t);
    }

    @Transactional
    public Task uncomplete(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new RuntimeException("Task não encontrada"));
        t.setStatus(TaskStatus.ACTIVE);
        t.setCompletedAt(null);
        return repo.save(t);
    }

    @Transactional
    public Task softDelete(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new RuntimeException("Task não encontrada"));
        t.setStatus(TaskStatus.DELETED);
        t.setDeletedAt(LocalDateTime.now());
        return repo.save(t);
    }

    @Transactional
    public Task restore(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new RuntimeException("Task não encontrada"));
        t.setStatus(TaskStatus.ACTIVE);
        t.setDeletedAt(null);
        return repo.save(t);
    }

    @Transactional
    public void purge(Long id) {
        repo.deleteById(id);
    }
}

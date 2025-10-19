package com.heilsalsa.foconatarefa.controller;

import com.heilsalsa.foconatarefa.domain.TaskStatus;
import com.heilsalsa.foconatarefa.model.Task;
import com.heilsalsa.foconatarefa.repository.TaskRepository;
import com.heilsalsa.foconatarefa.service.TaskService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskRepository repo;
    private final TaskService service;

    public TaskController(TaskRepository repo, TaskService service) {
        this.repo = repo;
        this.service = service;
    }

    // LISTAR com filtros (status + período) - paginado
    @GetMapping
    public Page<Task> list(
        @RequestParam(name = "status", defaultValue = "ACTIVE") TaskStatus status,
        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(name = "to", required = false)   @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to,
        @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (status == TaskStatus.COMPLETED && from != null && to != null) {
            return repo.findByDeletedAtIsNullAndStatusAndCompletedAtBetween(status, from, to, pageable);
        }
        return repo.findByDeletedAtIsNullAndStatus(status, pageable);
    }


    // BUSCAR por id
    @GetMapping("/{id}")
    public Task buscar(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Task não encontrada"));
    }

    // CRIAR
    @PostMapping
    public ResponseEntity<Task> criar(@RequestBody Task task) {
        Task salvo = repo.save(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + salvo.getId())).body(salvo);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public Task atualizar(@PathVariable Long id, @RequestBody Task novaTask) {
        return repo.findById(id)
                .map(t -> {
                    t.setTitulo(novaTask.getTitulo());
                    t.setDescricao(novaTask.getDescricao());
                    return repo.save(t);
                })
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));
    }

 // CONCLUIR - COMPLETE
    @PatchMapping("/{id}/complete")
    public Task complete(@PathVariable("id") Long id) {
        return service.complete(id);
    }

    // DESFAZER CONCLUSÃO
    @PatchMapping("/{id}/reopen")
    public Task reopen(@PathVariable("id") Long id) {
        return service.uncomplete(id);
    }

    // EXCLUIR (SOFT DELETE) – retorna a Task com status=DELETED
    @DeleteMapping("/{id}")
    public ResponseEntity<Task> softDelete(@PathVariable("id") Long id) {
        Task deleted = service.softDelete(id);
        return ResponseEntity.ok(deleted);
    }

    // RESTAURAR
    @PostMapping("/{id}/restore")
    public Task restore(@PathVariable("id") Long id) {
        return service.restore(id);
    }

    // EXCLUSÃO DEFINITIVA
    @Operation(summary = "Exclui definitivamente a tarefa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Task não encontrada")
    })
    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> purge(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable("id") Long id
    ) {
        service.purge(id);
        return ResponseEntity.noContent().build(); // 204
    }

}

package com.heilsalsa.foconatarefa.controller;

import com.heilsalsa.foconatarefa.domain.TaskStatus;
import com.heilsalsa.foconatarefa.model.Task;
import com.heilsalsa.foconatarefa.service.TaskService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Task> list(
            @RequestParam(name = "status", defaultValue = "ACTIVE") TaskStatus status,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return service.findActive(status, from, to, pageable);
    }

    @GetMapping("/trash")
    public Page<Task> listTrash(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return service.findDeleted(pageable);
    }

    @PatchMapping("/{id}/complete")
    public Task complete(@PathVariable("id") Long id) {
        return service.complete(id);
    }

    @PostMapping
    public ResponseEntity<Task> criar(@Valid @RequestBody Task task) {
        Task salvo = service.create(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + salvo.getId()))
                .body(salvo);
    }

    @PatchMapping("/{id}/reopen")
    public Task reopen(@PathVariable("id") Long id) {
        return service.uncomplete(id);
    }

    @PatchMapping("/{id}/soft-delete")
    public Task softDelete(@PathVariable("id") Long id) {
        return service.softDelete(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable("id") Long id) {
        service.purge(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public Task restore(@PathVariable("id") Long id) {
        return service.restore(id);
    }
}
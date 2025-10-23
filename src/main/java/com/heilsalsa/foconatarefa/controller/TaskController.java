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
//... imports permanecem

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

 // === LISTA ATUAIS (mantém)
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

 // === NOVO: LISTAR LIXEIRA (para o front /api/tasks/trash)
 @GetMapping("/trash")
 public Page<Task> listTrash(
     @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
 ) {
     // ajuste para o que você tem no repository. Exemplo:
     return repo.findByStatus(TaskStatus.DELETED, pageable);
     // Se não houver esse método, crie no repository:
     // Page<Task> findByStatus(TaskStatus status, Pageable pageable);
 }

 // === buscar, criar, atualizar permanecem ===

 // CONCLUIR - COMPLETE (ok)
 @PatchMapping("/{id}/complete")
 public Task complete(@PathVariable("id") Long id) {
     return service.complete(id);
 }

 // CRIAR
 @PostMapping 
 public ResponseEntity<Task> criar(@RequestBody Task task) { 
	 Task salvo = repo.save(task); 
	 return ResponseEntity.created
			 (URI.create("/api/tasks/" + salvo.getId())).body(salvo); }
 
 
 // DESFAZER CONCLUSÃO (ok)
 @PatchMapping("/{id}/reopen")
 public Task reopen(@PathVariable("id") Long id) {
     return service.uncomplete(id);
 }

 // ❌ ANTES: @DeleteMapping("/{id}") fazia SOFT delete
 // ✅ AGORA: SOFT DELETE no caminho que o app usa
 @PatchMapping("/{id}/soft-delete")
 public Task softDelete(@PathVariable("id") Long id) {
     return service.softDelete(id);
 }

 // ✅ HARD DELETE no caminho que o app usa
 @DeleteMapping("/{id}")
 public ResponseEntity<Void> hardDelete(@PathVariable("id") Long id) {
     service.purge(id);
     return ResponseEntity.noContent().build();
 }

 // (Opcional) manter o alias antigo se quiser compatibilidade
 @DeleteMapping("/{id}/purge")
 public ResponseEntity<Void> purge(@PathVariable("id") Long id) {
     service.purge(id);
     return ResponseEntity.noContent().build();
 }

 // (Opcional) manter restore se já estiver usando em outro fluxo
 @PostMapping("/{id}/restore")
 public Task restore(@PathVariable("id") Long id) {
     return service.restore(id);
 }
}

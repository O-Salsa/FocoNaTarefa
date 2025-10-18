package com.heilsalsa.foconatarefa.controller;

import com.heilsalsa.foconatarefa.exceptions.TaskNotFoundException;
import com.heilsalsa.foconatarefa.model.Task;
import com.heilsalsa.foconatarefa.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import jakarta.validation.Valid;



@RestController // indica que os metodos vao responder por API REST
@RequestMapping("/api/tasks") // URL base do recurso
public class TaskController {

	@Autowired
	private TaskRepository repository;
	
	//get /api/tasks
	@GetMapping
	public List<Task> listar(){
		return repository.findAll();
		}
	
	// GET /api/tasks/{id}
	@GetMapping("/{id}")
	public Task buscar(@PathVariable("id") Long id) {
		return repository.findById(id)
			.orElseThrow(() -> new TaskNotFoundException(id));
	}
	
	// POST /api/tasks
	@PostMapping
	public Task criar(@Valid @RequestBody Task task) {
		return repository.save(task);
	}
	
	//PUT /api/tasks/{id}
	@PutMapping("/{id}")
	public Task atualizar(@PathVariable("id") Long id,@Valid @RequestBody Task novaTask) {
		return repository.findById(id)
				.map(task -> {
					task.setTitulo(novaTask.getTitulo());
					task.setDescricao(novaTask.getDescricao());
					task.setFeito(novaTask.getFeito());
					task.setHorarioLimite(novaTask.getHorarioLimite());
					return repository.save(task);
					})
				.orElseThrow(() -> new RuntimeException("Tarefa não encontrada")); 
	}
	
	// DELETE /api/tasks/{id}
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable("id") Long id) {
		repository.deleteById(id);
	}
	
	
	
	
}


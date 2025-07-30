package com.heilsalsa.foconatarefa.TaskController;

import com.heilsalsa.foconatarefa.model.Task;
import com.heilsalsa.foconatarefa.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
	public Optional<Task> buscar(@PathVariable Long id) {
		return repository.findById(id);
	}
	
	// POST /api/tasks
	@PostMapping
	public Task criar(@RequestBody Task task) {
		return repository.save(task);
	}
	
	//PUT /api/tasks/{id}
	@PutMapping("/{id}")
	public Task atualizar(@PathVariable Long id, @RequestBody Task novaTask) {
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
	public void deletar(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
}


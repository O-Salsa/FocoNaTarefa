package com.heilsalsa.foconatarefa.foconatarefa;

public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(Long id) {
		super("Tarefa não encontrada com id: " + id);
	}
}

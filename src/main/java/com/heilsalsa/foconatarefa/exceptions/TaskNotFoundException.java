package com.heilsalsa.foconatarefa.exceptions;

public class TaskNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskNotFoundException(Long id) {
		super("Tarefa não encontrada com id: " + id);
	}
}

package com.heilsalsa.foconatarefa.repository;

import com.heilsalsa.foconatarefa.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

//Nao implementar o Spring ja faz isso sozinho
public interface TaskRepository extends JpaRepository<Task, Long> {

	
}

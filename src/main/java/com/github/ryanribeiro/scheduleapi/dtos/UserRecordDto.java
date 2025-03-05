package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

<<<<<<< HEAD
import com.github.ryanribeiro.scheduleapi.entities.TarefaModel;
=======
import com.github.ryanribeiro.scheduleapi.entities.TaskModel;
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
import com.github.ryanribeiro.scheduleapi.enums.RoleName;

import jakarta.validation.constraints.NotBlank;


public record UserRecordDto (
		@NotBlank
		String name,
		
		@NotBlank
		String username,
		
		@NotBlank
		String email,
		
		@NotBlank
		String password,
		
<<<<<<< HEAD
		List<TarefaModel> tarefas,
		
		Set<RoleName> roles,
		
		Date dataInclusao){
=======
		List<TaskModel> tarefas,
		
		Set<RoleName> roles,
		
		Date inclusionDate){
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
	
	
}


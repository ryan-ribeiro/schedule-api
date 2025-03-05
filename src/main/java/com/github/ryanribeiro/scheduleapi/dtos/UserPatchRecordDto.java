package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

<<<<<<< HEAD
import com.github.ryanribeiro.scheduleapi.entities.TarefaModel;
=======
import com.github.ryanribeiro.scheduleapi.entities.TaskModel;
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)

public record UserPatchRecordDto (
		String name,
		
		String username,
		
		String password,
		
<<<<<<< HEAD
		List<TarefaModel> tarefas,
		
		Set<String> roles,
		
		Date dataInclusao){
=======
		List<TaskModel> tarefas,
		
		Set<String> roles,
		
		Date inclusionDate){
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
}
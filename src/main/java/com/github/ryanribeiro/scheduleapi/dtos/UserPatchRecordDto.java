package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.github.ryanribeiro.scheduleapi.entities.TaskModel;

public record UserPatchRecordDto (
		String name,
		
		String username,
		
		String password,
		
		List<TaskModel> tarefas,
		
		Set<String> roles,
		
		Date inclusionDate){
}
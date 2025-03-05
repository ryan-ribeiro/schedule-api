package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.github.ryanribeiro.scheduleapi.entities.TaskModel;
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
		
		List<TaskModel> tarefas,
		
		Set<RoleName> roles,
		
		Date inclusionDate){
	
	
}


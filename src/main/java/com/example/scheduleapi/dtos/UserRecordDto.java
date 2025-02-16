package com.example.scheduleapi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.scheduleapi.models.TarefaModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRecordDto (
		@NotNull
		String name,
		
		@NotNull
		String username,
		
		@NotBlank
		String password,
		
		List<TarefaModel> tarefas,
		
		@NotNull
		Set<String> roles,
		
//		@NotNull
		Date dataInclusao){
}

package com.example.scheduleapi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.scheduleapi.models.TarefaModel;

import jakarta.validation.constraints.NotNull;

public record UserPatchRecordDto (
		String name,
		
		String username,
		
		String password,
		
		List<TarefaModel> tarefas,
		
		Set<String> roles,
		
		Date dataInclusao){
}
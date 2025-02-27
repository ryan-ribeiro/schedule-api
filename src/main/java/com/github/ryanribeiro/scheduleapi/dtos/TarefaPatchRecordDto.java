package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;

import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.enums.Status;

public record TarefaPatchRecordDto(
		String nome, 
		
		Date dataInsercao,
		
		Date dataFinal,
		
		Status status,
		
		UserModel usuario,
		
		String uri
		
		) {
	
}

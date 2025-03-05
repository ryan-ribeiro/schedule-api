package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;

import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.enums.Status;

public record TaskPatchRecordDto(
		String name, 
		
		Date inclusionDate,
		
		Date finalDate,
		
		Status status,
		
		UserModel usuario,
		
		String uri
		
		) {
	
}

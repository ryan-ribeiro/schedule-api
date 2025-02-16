package com.example.scheduleapi.dtos;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.example.scheduleapi.models.UserModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TarefaPatchRecordDto(
		String nome, 
		
		Date dataInsercao,
		
		Date dataFinal, 
		
		UserModel usuario,
		
		String uri) {
	
}

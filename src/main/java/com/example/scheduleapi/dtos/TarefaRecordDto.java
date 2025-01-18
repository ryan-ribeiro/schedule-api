package com.example.scheduleapi.dtos;



import java.util.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record TarefaRecordDto (
		@NotBlank(message = "O campo nome não pode ser vazio")
		@Length(min = 3, max = 255, message = "O campo nome deverá conter entre 3 a 255 caracters!")
		String nome, 
		
		@Valid
		Date dataInsercao,
		
		@Valid
//		@NotNull
		Date dataFinal, 
		
		@Valid 
		String uri) {

}

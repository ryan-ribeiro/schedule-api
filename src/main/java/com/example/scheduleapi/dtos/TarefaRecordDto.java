package com.example.scheduleapi.dtos;



import java.util.Date;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.example.scheduleapi.models.TarefaModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TarefaRecordDto (
		@NotBlank(message = "O campo 'nome' não pode ser vazio")
		@Length(min = 3, max = 255, message = "O campo nome deverá conter entre 3 a 255 caracters!")
		String nome, 
		
		@NotNull(message = "A data de inserção não pode ser nula")
		Date dataInclusao,
		
		@NotNull(message = "A data final não pode ser nula")
		Date dataFinal, 
		
		@NotNull
		UUID usuarioId,
		
		@NotBlank(message = "A URI não pode ser vazia")
	    @Pattern(regexp = "^(https?|ftp)://[a-zA-Z0-9.-]+(:[0-9]+)?(/.*)?$", message = "URI inválida")
		String uri) {

}

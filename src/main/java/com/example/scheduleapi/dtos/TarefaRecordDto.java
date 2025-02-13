package com.example.scheduleapi.dtos;



import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

//@JsonIgnoreProperties(ignoreUnknown = true)
public record TarefaRecordDto (
		@NotBlank(message = "O campo 'username' não pode ser vazio")
		@Length(min = 3, max = 255, message = "O campo nome deverá conter entre 3 a 255 caracters!")
		String username,
		
		@NotBlank(message = "O campo 'password' não pode ser vazio")
		@Length(min = 3, max = 255, message = "O campo nome deverá conter entre 3 a 255 caracters!")
		String password,
		
		@NotBlank(message = "O campo 'nome' não pode ser vazio")
		@Length(min = 3, max = 255, message = "O campo nome deverá conter entre 3 a 255 caracters!")
		String nome, 
		
		@NotNull(message = "A data de inserção não pode ser nula")
		Date dataInsercao,
		
		@NotNull(message = "A data final não pode ser nula")
		Date dataFinal, 
		
		@NotBlank(message = "A URI não pode ser vazia")
	    @Pattern(regexp = "^(https?|ftp)://[a-zA-Z0-9.-]+(:[0-9]+)?(/.*)?$", message = "URI inválida")
		String uri) {

}

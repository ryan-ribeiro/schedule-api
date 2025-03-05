package com.github.ryanribeiro.scheduleapi.dtos;

import java.util.Date;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.ryanribeiro.scheduleapi.enums.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder.Default;

public record TaskRecordDto (
		@NotBlank(message = "O campo 'nome' não pode ser vazio")
		@Length(min = 3, max = 255, message = "O campo nome deverá conter entre 3 a 255 caracters!")
		String name, 
		
		@NotNull(message = "A data de inserção não pode ser nula")
		Date inclusionDate,
		
		@NotNull(message = "A data final não pode ser nula")
		Date finalDate, 
		
		@NotNull
		Status status,
		
		@NotNull
		UUID userId,
		
		@NotBlank(message = "A URI não pode ser vazia")
	    @Pattern(regexp = "^(https?|ftp)://[a-zA-Z0-9.-]+(:[0-9]+)?(/.*)?$", message = "URI inválida")
		String uri) {

}


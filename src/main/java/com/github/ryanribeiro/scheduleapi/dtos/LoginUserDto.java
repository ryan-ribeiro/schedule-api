package com.github.ryanribeiro.scheduleapi.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(
		
		@NotBlank
        String email,
        @NotBlank
        String password

) {
}

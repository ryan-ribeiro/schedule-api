package com.example.scheduleapi.configs;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Configuration
public class DateConfig {
	public static final String DATETIME_FORMAT	= "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static LocalDateSerializer LOCAL_DATETIME_SERIALIZER = 
			new LocalDateSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
	
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(LOCAL_DATETIME_SERIALIZER);
		return new ObjectMapper()
					.registerModule(module)
					.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
}

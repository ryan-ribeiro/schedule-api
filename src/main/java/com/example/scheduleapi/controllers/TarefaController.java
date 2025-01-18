package com.example.scheduleapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduleapi.dtos.TarefaRecordDto;
import com.example.scheduleapi.models.TarefaModel;
import com.example.scheduleapi.repositories.TarefaRepository;
import com.example.scheduleapi.services.NullPropertyNamesServices;

import jakarta.validation.Valid;

@RestController
public class TarefaController{
	@Autowired
	TarefaRepository tarefaRepository;
	
	@Autowired
	NullPropertyNamesServices nullPropertyNames;
	
	@PostMapping("/tarefa")
	public ResponseEntity<TarefaModel> cadastrarTarefa(@RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
		var tarefaModel = new TarefaModel();
		BeanUtils.copyProperties(tarefaRecordDto, tarefaModel);
		tarefaModel.setDataInclusao(new Date());
		return ResponseEntity.status(HttpStatus.OK).body(tarefaRepository.save(tarefaModel));
	}
	
	@GetMapping("/tarefa")
	public ResponseEntity<Object> getAllTarefas(@PageableDefault(page= 0, size= 10, sort= "dataInclusao", 
												direction= Sort.Direction.ASC) Pageable pageable) {
	    try {
	    	Page<TarefaModel> tarefaPage = tarefaRepository.findAll(pageable);
	        if (!tarefaPage.isEmpty()) {
	            for (TarefaModel tarefa : tarefaPage) {
	                UUID id = tarefa.getId();
	                tarefa.add(linkTo(methodOn(TarefaController.class).getOneTarefa(id, pageable)).withSelfRel());
	            }
	        }
	        return ResponseEntity.status(HttpStatus.OK).body(tarefaPage);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	        		.body("Database is not accessible. Please try again later.");
	    }
	}

	@GetMapping("/tarefa/{id}")
	public ResponseEntity<Object> getOneTarefa(@PathVariable(value="id")UUID id, @PageableDefault(page= 0, size= 10, sort= "dataInclusao",
																			direction= Sort.Direction.ASC) Pageable pageable) {
		Optional<TarefaModel> tarefa = tarefaRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		tarefa.get().add(linkTo(methodOn(TarefaController.class).getAllTarefas(pageable)).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(tarefa.get());
	}
	
//	@PatchMapping("/tarefa/{id}")
//	public ResponseEntity<Object> patchTarefa(@PathVariable(value="id") UUID id,
//											   @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
//		Optional<TarefaModel> tarefaOptional = tarefaRepository.findById(id);
//		if(tarefaOptional.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
//		}
//		var tarefaModel = tarefaOptional.get();
//		
//	    
//		tarefaModel.setNome(tarefaRecordDto.nome());
//		tarefaModel.setDataFinal(tarefaRecordDto.dataFinal());
//		
//		tarefaModel = tarefaRepository.save(tarefaModel);
//		
//		return ResponseEntity.status(HttpStatus.OK).body(tarefaModel);
//	}
	
	@PatchMapping("/tarefa/{id}")
	public ResponseEntity<Object> patchTarefa(@PathVariable(value="id") UUID id,
											   @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
		Optional<TarefaModel> tarefaOptional = tarefaRepository.findById(id);
		if(tarefaOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		var tarefaModel = tarefaOptional.get();
		
	    BeanUtils.copyProperties(tarefaRecordDto, tarefaModel, nullPropertyNames.getNullPropertyNames(tarefaRecordDto));
		
		tarefaModel = tarefaRepository.save(tarefaModel);
		
		return ResponseEntity.status(HttpStatus.OK).body(tarefaModel);
	}
	
	@PutMapping("/tarefa/{id}")
	public ResponseEntity<Object> putTarefa(@PathVariable(value="id") UUID id,
											   @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
		Optional<TarefaModel> tarefa = tarefaRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		var tarefaModel = tarefa.get();
	    
		BeanUtils.copyProperties(tarefaRecordDto, tarefaModel);
		return ResponseEntity.status(HttpStatus.OK).body(tarefaRepository.save(tarefaModel));
	}
	
	@DeleteMapping("/tarefa/{id}")
	public ResponseEntity<Object> deleteTarefa(@PathVariable(value="id") UUID id) {
		Optional<TarefaModel> tarefa = tarefaRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found.");
		}
		tarefaRepository.delete(tarefa.get());
		return ResponseEntity.status(HttpStatus.OK).body("Tarefa deleted succesfully.");
	}
}

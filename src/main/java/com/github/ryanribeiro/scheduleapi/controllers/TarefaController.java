package com.github.ryanribeiro.scheduleapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ryanribeiro.scheduleapi.dtos.TarefaPatchRecordDto;
import com.github.ryanribeiro.scheduleapi.dtos.TarefaRecordDto;
import com.github.ryanribeiro.scheduleapi.entities.TarefaModel;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.enums.Status;
import com.github.ryanribeiro.scheduleapi.repository.TarefaRepository;
import com.github.ryanribeiro.scheduleapi.repository.UserRepository;
import com.github.ryanribeiro.scheduleapi.services.AuthenticationService;
import com.github.ryanribeiro.scheduleapi.services.NullPropertyNamesServices;

import jakarta.validation.Valid;

@RestController
public class TarefaController{
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NullPropertyNamesServices nullPropertyNames;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/tarefa")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<TarefaModel> postTarefas(@RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
	    UserModel usuario = userRepository.findById(tarefaRecordDto.usuarioId())
	            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication == null) {
	        authentication = authenticationService.autenticarUsuario(usuario);
	    }
	    
	    String username = authentication.getName();
	    if (!username.equals(usuario.getEmail())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	    
	    TarefaModel tarefa = new TarefaModel();
	    BeanUtils.copyProperties(tarefaRecordDto, tarefa);
	    tarefa.setUsuario(usuario);
	    
	    if(tarefaRecordDto.status() == null) {
	    	tarefa.setStatus(Status.PARA_FAZER);
	    }

	    return ResponseEntity.ok(tarefaRepository.save(tarefa));
	}
	
	@GetMapping("/tarefa/user/{username}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<List<TarefaModel>> getUserTarefas(@PathVariable(value = "username") String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nomeUsuario = authentication.getName();
		
		UserModel user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!user.getEmail().equals(nomeUsuario)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		List<TarefaModel> tarefas = tarefaRepository.findByUsername(username);
		
		if(tarefas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tarefas);
		}
		
		return ResponseEntity.ok(tarefas);
	}
	
	@GetMapping("/tarefa")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
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
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> getOneTarefa(@PathVariable(value="id")UUID id, @PageableDefault(page= 0, size= 10, sort= "dataInclusao",
																			direction= Sort.Direction.ASC) Pageable pageable) {
		Optional<TarefaModel> tarefa = tarefaRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(userRepository.findById(tarefa.get().getUsuario().getId()).get().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		tarefa.get().add(linkTo(methodOn(TarefaController.class).getAllTarefas(pageable)).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(tarefa.get());
	}
	
	@PatchMapping("/tarefa/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> patchTarefa(@PathVariable(value="id") UUID id,
											   @RequestBody TarefaPatchRecordDto tarefaPatchRecordDto) {
		Optional<TarefaModel> tarefaOptional = tarefaRepository.findById(id);
		if(tarefaOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		var tarefaModel = tarefaOptional.get();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(tarefaModel.getUsuario().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	    BeanUtils.copyProperties(tarefaPatchRecordDto, tarefaModel, nullPropertyNames.getNullPropertyNames(tarefaPatchRecordDto));
		
		tarefaModel = tarefaRepository.save(tarefaModel);
		
		return ResponseEntity.status(HttpStatus.OK).body(tarefaModel);
	}
	
	@PutMapping("/tarefa/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> putTarefa(@PathVariable(value="id") UUID id,
											   @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
		Optional<TarefaModel> tarefa = tarefaRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		var tarefaModel = tarefa.get();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(tarefaModel.getUsuario().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		if(tarefaRecordDto.usuarioId() != null) {
			Optional<UserModel> userOptional = userRepository.findById(tarefaRecordDto.usuarioId());
	        if (userOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	        tarefaModel.setUsuario(userOptional.get());
		}
	    
		BeanUtils.copyProperties(tarefaRecordDto, tarefaModel, "usuario");
		return ResponseEntity.status(HttpStatus.OK).body(tarefaRepository.save(tarefaModel));
	}
	
	@DeleteMapping("/tarefa/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> deleteTarefa(@PathVariable(value="id") UUID id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		Optional<TarefaModel> tarefa = tarefaRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found.");
		}
		
		if(!usernameAuth.equals(tarefa.get().getUsuario().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		tarefaRepository.delete(tarefa.get());
		return ResponseEntity.status(HttpStatus.OK).body("Tarefa deleted succesfully.");
	}
}

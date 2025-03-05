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

import com.github.ryanribeiro.scheduleapi.dtos.TaskPatchRecordDto;
import com.github.ryanribeiro.scheduleapi.dtos.TaskRecordDto;
import com.github.ryanribeiro.scheduleapi.entities.TaskModel;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.enums.Status;
import com.github.ryanribeiro.scheduleapi.repository.TaskRepository;
import com.github.ryanribeiro.scheduleapi.repository.UserRepository;
import com.github.ryanribeiro.scheduleapi.services.AuthenticationService;
import com.github.ryanribeiro.scheduleapi.services.NullPropertyNamesServices;

import jakarta.validation.Valid;

@RestController
public class TaskController{
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NullPropertyNamesServices nullPropertyNames;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/task")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<TaskModel> postTask(@RequestBody @Valid TaskRecordDto taskRecordDto) {
		System.out.println("Buscando usuário com ID: " + taskRecordDto.userId());
	    UserModel usuario = userRepository.findById(taskRecordDto.userId())
	            .orElseThrow(() -> new RuntimeException("User not found."));

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication == null) {
	        authentication = authenticationService.autenticarUsuario(usuario);
	    }
	    
	    String username = authentication.getName();
	    if (!username.equals(usuario.getEmail())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	    
	    TaskModel tarefa = new TaskModel();
	    BeanUtils.copyProperties(taskRecordDto, tarefa);
	    tarefa.setUser(usuario);
	    
	    if(taskRecordDto.status() == null) {
	    	tarefa.setStatus(Status.PARA_FAZER);
	    }

	    return ResponseEntity.ok(taskRepository.save(tarefa));
	}
	
	@GetMapping("/task/user/{username}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<List<TaskModel>> getUserTasks(@PathVariable(value = "username") String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		UserModel user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!user.getEmail().equals(userName)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		List<TaskModel> tasks = taskRepository.findByUsername(username);
		
		if(tasks.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tasks);
		}
		
		return ResponseEntity.ok(tasks);
	}
	
	@GetMapping("/task")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<Object> getAllTasks(@PageableDefault(page= 0, size= 10, sort= "inclusionDate", 
												direction= Sort.Direction.ASC) Pageable pageable) {
	    try {
	    	Page<TaskModel> taskPage = taskRepository.findAll(pageable);
	        if (!taskPage.isEmpty()) {
	            for (TaskModel task : taskPage) {
	                UUID id = task.getId();
	                task.add(linkTo(methodOn(TaskController.class).getOneTask(id, pageable)).withSelfRel());
	            }
	        }
	        return ResponseEntity.status(HttpStatus.OK).body(taskPage);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	        		.body("Database is not accessible. Please try again later.");
	    }
	}

	@GetMapping("/task/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> getOneTask(@PathVariable(value="id")UUID id, @PageableDefault(page= 0, size= 10, sort= "inclusionDate",
																			direction= Sort.Direction.ASC) Pageable pageable) {
		Optional<TaskModel> task = taskRepository.findById(id);
		if(task.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(userRepository.findById(task.get().getUser().getId()).get().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		task.get().add(linkTo(methodOn(TaskController.class).getAllTasks(pageable)).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(task.get());
	}
	
	@PatchMapping("/task/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> patchTask(@PathVariable(value="id") UUID id,
											   @RequestBody TaskPatchRecordDto taskPatchRecordDto) {
		Optional<TaskModel> taskOptional = taskRepository.findById(id);
		if(taskOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		var tarefaModel = taskOptional.get();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(tarefaModel.getUser().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	    BeanUtils.copyProperties(taskPatchRecordDto, tarefaModel, nullPropertyNames.getNullPropertyNames(taskPatchRecordDto));
		
		tarefaModel = taskRepository.save(tarefaModel);
		
		return ResponseEntity.status(HttpStatus.OK).body(tarefaModel);
	}
	
	@PutMapping("/task/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> putTask(@PathVariable(value="id") UUID id,
											   @RequestBody @Valid TaskRecordDto taskRecordDto) {
		Optional<TaskModel> task = taskRepository.findById(id);
		if(task.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found");
		}
		var tarefaModel = task.get();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(tarefaModel.getUser().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		if(taskRecordDto.userId() != null) {
			Optional<UserModel> userOptional = userRepository.findById(taskRecordDto.userId());
	        if (userOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	        tarefaModel.setUser(userOptional.get());
		}
	    
		BeanUtils.copyProperties(taskRecordDto, tarefaModel, "usuario");
		return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(tarefaModel));
	}
	
	@DeleteMapping("/task/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> deleteTask(@PathVariable(value="id") UUID id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		Optional<TaskModel> tarefa = taskRepository.findById(id);
		if(tarefa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa not found.");
		}
		
		if(!usernameAuth.equals(tarefa.get().getUser().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		taskRepository.delete(tarefa.get());
		return ResponseEntity.status(HttpStatus.OK).body("Tarefa deleted succesfully.");
	}
}

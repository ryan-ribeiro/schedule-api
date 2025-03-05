package com.github.ryanribeiro.scheduleapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ryanribeiro.scheduleapi.dtos.UserPatchRecordDto;
import com.github.ryanribeiro.scheduleapi.dtos.UserRecordDto;
import com.github.ryanribeiro.scheduleapi.entities.TaskModel;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.repository.TaskRepository;
import com.github.ryanribeiro.scheduleapi.repository.UserRepository;
import com.github.ryanribeiro.scheduleapi.services.NullPropertyNamesServices;
import com.github.ryanribeiro.scheduleapi.services.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	NullPropertyNamesServices nullPropertyNames;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<Object> getAllUsers(@PageableDefault(page= 0, size= 10, sort= "inclusionDate", 
			direction= Sort.Direction.ASC) Pageable pageable) {
		try {
			Page<UserModel> usersPage = userRepository.findAll(pageable);
			if(!usersPage.isEmpty()) {
				for(UserModel user: usersPage) {
					UUID id = user.getId();
					user.add(linkTo(methodOn(TaskController.class).getOneTask(id, pageable)).withSelfRel());
				}
			}
			return ResponseEntity.ok(usersPage);
		}
		catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	        		.body("Database is not accessible. Please try again later.");
	    }
	}
	
	@GetMapping("/user/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> getOneUser(@PathVariable(value="id")UUID id, @PageableDefault(page= 0, size= 10, sort= "inclusionDate",
																			direction= Sort.Direction.ASC) Pageable pageable) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(!username.equals(userRepository.findById(id).get().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		Optional<UserModel> user = userRepository.findById(id);
		if(user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Uesr not found");
		}
		user.get().add(linkTo(methodOn(TaskController.class).getAllTasks(pageable)).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(user.get());
	}
	
	@PatchMapping("/user/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> patchUser(@PathVariable(value="id") UUID id,
											   @RequestBody @Valid UserPatchRecordDto userPatchRecordDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(userRepository.findById(id).get().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		Optional<UserModel> userOptional = userRepository.findById(id);
		if(userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
		var userModel = userOptional.get();
		
	    BeanUtils.copyProperties(userPatchRecordDto, userModel, nullPropertyNames.getNullPropertyNames(userPatchRecordDto));
	    userModel.setPassword(passwordEncoder.encode(userPatchRecordDto.password()));
	    
		userModel = userRepository.save(userModel);
		
		return ResponseEntity.status(HttpStatus.OK).body(userModel);
	}
	
	@PutMapping("/user/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> putUser(@PathVariable(value = "id") UUID id,
	                                      @RequestBody @Valid UserRecordDto userRecordDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(userRepository.findById(id).get().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	    Optional<UserModel> userOptional = userRepository.findById(id);
	    if (userOptional.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    
	    var userModel = userOptional.get();
	    BeanUtils.copyProperties(userRecordDto, userModel, "tarefas");
	    userModel.setPassword(passwordEncoder.encode(userRecordDto.password()));
	    
	    if (userRecordDto.tarefas() != null) {
	        // Atualizando apenas as tarefas passadas sem remover as antigas
	        userRecordDto.tarefas().forEach(tarefaDto -> {
	        	
	            Optional<TaskModel> tarefaOptional = taskRepository.findById(tarefaDto.getId());
	            if (tarefaOptional.isPresent()) {
	                TaskModel taskModel = tarefaOptional.get();
	                BeanUtils.copyProperties(tarefaDto, taskModel, "id", "usuario");
	                taskModel.setUser(userModel);
	            } else {
	                TaskModel novaTarefa = new TaskModel();
	                BeanUtils.copyProperties(tarefaDto, novaTarefa);
	                novaTarefa.setUser(userModel);
	                userModel.getTasks().add(novaTarefa);
	            }
	        });
	    }
	    
	    try {
	        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving user: " + e.getMessage());
	    }
	}
	
	@DeleteMapping("/user/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Object> deleteUser(@PathVariable(value="id") UUID id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameAuth = authentication.getName();
		
		if(!usernameAuth.equals(userRepository.findById(id).get().getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		Optional<UserModel> user = userRepository.findById(id);
		if(user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		userRepository.delete(user.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted succesfully.");
	}
}
package com.github.ryanribeiro.scheduleapi.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.github.ryanribeiro.scheduleapi.dtos.CreateUserDto;
import com.github.ryanribeiro.scheduleapi.dtos.LoginUserDto;
import com.github.ryanribeiro.scheduleapi.dtos.RecoveryJwtTokenDto;
import com.github.ryanribeiro.scheduleapi.dtos.UserRecordDto;
import com.github.ryanribeiro.scheduleapi.entities.Role;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.repository.RoleRepository;
import com.github.ryanribeiro.scheduleapi.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
    private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
//	@PostMapping("/register")
//    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRecordDto userRecordDto) throws Exception {
//    	UserModel user = new UserModel();
//		BeanUtils.copyProperties(userRecordDto, user);
//		
//	    Set<Role> roles = userRecordDto.roles().stream()
//	        .map(roleName -> roleRepository.findByName(roleName)
//	            .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")))
//	        .collect(Collectors.toSet());
//
//	    user.setRoles(roles);
//		
//		userService.createUser(user);
//		
//		try {
//			return ResponseEntity.ok().build();
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
//    }
	
	@PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRecordDto userRecordDto) throws Exception {
		try {
			userService.createUser(userRecordDto);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
    }
	
	@PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody @Valid LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
	
	@GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }
}

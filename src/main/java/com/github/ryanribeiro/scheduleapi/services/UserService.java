package com.github.ryanribeiro.scheduleapi.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.ryanribeiro.scheduleapi.configs.security.UserDetailsImpl;
//import com.github.ryanribeiro.scheduleapi.dtos.CreateUserDto;
import com.github.ryanribeiro.scheduleapi.dtos.LoginUserDto;
import com.github.ryanribeiro.scheduleapi.dtos.RecoveryJwtTokenDto;
import com.github.ryanribeiro.scheduleapi.dtos.UserRecordDto;
import com.github.ryanribeiro.scheduleapi.entities.Role;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.repository.RoleRepository;
import com.github.ryanribeiro.scheduleapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	private RoleRepository roleRepository;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

 // Method responsible for authenticating a user and returning a JWT token
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Creates an authentication object with the user's email and password
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Authenticates the user with the provided credentials
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Gets the UserDetails object of the authenticated user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generates a JWT token for the authenticated user
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    
    public void createUser(UserModel user) {
    	Optional<UserModel> userOptional = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
    	
    	if(userOptional.isEmpty()) {
			String pass = user.getPassword();
			user.setPassword(passwordEncoder.encode(pass));
			userRepository.save(user);
    	}
	}
    
    public void createUser(UserRecordDto userRecordDto) {	
	    Set<Role> roles = userRecordDto.roles().stream()
	        .map(roleName -> roleRepository.findByName(roleName)
	            .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")))
	        .collect(Collectors.toSet());
	    
	    UserModel user = new UserModel();
		BeanUtils.copyProperties(userRecordDto, user);
	    user.setRoles(roles);
	
    	Optional<UserModel> userOptional = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
    	
    	if(userOptional.isEmpty()) {
			String pass = user.getPassword();
			user.setPassword(passwordEncoder.encode(pass));
			userRepository.save(user);
    	}
	}
}

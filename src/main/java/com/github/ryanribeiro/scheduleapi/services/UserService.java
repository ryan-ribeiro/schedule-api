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

 // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }
    
//    public void createUser(UserRecordDto userRecord) {
//    	
//    	Optional<UserModel> userOptional = Optional.ofNullable(userRepository.findByUsername(userRecord.username()));
//    	
//    	if(userOptional.isEmpty()) {
//    		// Converte cada nome de role (String) em um objeto Role
//    		Set<Role> roles = userRecord.roles().stream()
//                    .map(roleName -> Role.builder().name(RoleName.valueOf(roleName)).build())  // Converte para RoleName
//                    .collect(Collectors.toSet());
//
//    		
//	        // Cria um novo usuário com os dados fornecidos
//	        UserModel newUser = UserModel.builder()
//	        		.username(userRecord.username())
//	                // Codifica a senha do usuário com o algoritmo bcrypt
//	                .password(securityConfiguration.passwordEncoder().encode(userRecord.password()))
//	                .tarefas(userRecord.tarefas())
//	                // Atribui ao usuário uma permissão específica
//	                .roles(roles)
//	                .dataInclusao(userRecord.dataInclusao())
//	                .build();
//	
//	        // Salva o novo usuário no banco de dados
//	        userRepository.save(newUser);
//    	}
//    }
    
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

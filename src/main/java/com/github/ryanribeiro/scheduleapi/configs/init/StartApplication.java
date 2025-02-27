package com.github.ryanribeiro.scheduleapi.configs.init;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.ryanribeiro.scheduleapi.entities.Role;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.enums.RoleName;
import com.github.ryanribeiro.scheduleapi.repository.UserRepository;
import com.github.ryanribeiro.scheduleapi.services.UserService;

@Component
public class StartApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public StartApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
	public void run(String... args) throws Exception {
	    UserModel user = userRepository.findByUsername("admin");
	    if (user == null) {
	        user = new UserModel();
	        user.setUsername("admin");
	        user.setEmail("admin@gmail.com");
	        user.setPassword(passwordEncoder.encode("admin123"));

	        if (user.getRoles() == null) {
	            user.setRoles(new HashSet<>());
	        }

	        Role roleAdmin = new Role();
	        roleAdmin.setName(RoleName.ROLE_ADMINISTRATOR); // Corrigindo a atribuição do nome do papel
	        user.getRoles().add(roleAdmin);
	        
	        userRepository.save(user);
	    }

	    user = userRepository.findByUsername("user");
	    if (user == null) {
	        user = new UserModel();
	        user.setUsername("user");
	        user.setEmail("user@gmail.com");
	        user.setPassword(passwordEncoder.encode("user123"));

	        if (user.getRoles() == null) {
	            user.setRoles(new HashSet<>());
	        }

	        Role roleCustomer = new Role();
	        roleCustomer.setName(RoleName.ROLE_CUSTOMER);
	        user.getRoles().add(roleCustomer);

	        userRepository.save(user);
	    }
	}
}
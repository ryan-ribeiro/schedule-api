package com.example.scheduleapi.init;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.scheduleapi.models.UserModel;
import com.example.scheduleapi.repositories.UserRepository;

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
		if(user == null) {
			user = new UserModel();
			user.setName("ADMIN");
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("admin123"));
			if (user.getRoles() == null) {
			    user.setRoles(new HashSet<>());
			}
			user.getRoles().add("MANAGERS");
			userRepository.save(user);
		}
		
		user = userRepository.findByUsername("user");
		if(user == null) {
			user = new UserModel();
			user.setName("USER");
			user.setUsername("user");
			user.setPassword(passwordEncoder.encode("user123"));
			if (user.getRoles() == null) {
			    user.setRoles(new HashSet<>());
			}
			user.getRoles().add("USERS");
			userRepository.save(user);
		}
	}
}

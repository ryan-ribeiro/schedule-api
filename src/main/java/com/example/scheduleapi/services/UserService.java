package com.example.scheduleapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.scheduleapi.models.UserModel;
import com.example.scheduleapi.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	public void createUser(UserModel user) {
		String pass = user.getPassword();
		user.setPassword(passwordEncoder.encode(pass));
		userRepository.save(user);
	}
}

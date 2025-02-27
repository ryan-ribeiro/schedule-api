package com.github.ryanribeiro.scheduleapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ryanribeiro.scheduleapi.entities.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>{
	Optional<UserModel> findByEmail(String email);
	
	UserModel findByUsername(String username);
}

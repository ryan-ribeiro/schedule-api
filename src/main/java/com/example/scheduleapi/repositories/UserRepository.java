package com.example.scheduleapi.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scheduleapi.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{
	@Query("SELECT t FROM UserModel t JOIN FETCH t.roles WHERE t.username =:username")
    public UserModel findByUsername(@Param("username") String username);
}

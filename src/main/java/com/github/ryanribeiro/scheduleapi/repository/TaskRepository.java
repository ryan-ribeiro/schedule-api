package com.github.ryanribeiro.scheduleapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.ryanribeiro.scheduleapi.entities.TaskModel;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, UUID>{
	@Query("SELECT t FROM TaskModel t WHERE t.user.username =:username")
    public List<TaskModel> findByUsername(@Param("username") String username);
}

package com.example.scheduleapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scheduleapi.models.TarefaModel;

public interface TarefaRepository extends JpaRepository<TarefaModel, UUID>{
	@Query("SELECT t FROM TarefaModel t JOIN FETCH t.roles WHERE t.username =:username")
    public TarefaModel findByUsername(@Param("username") String username);
}

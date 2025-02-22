package com.example.scheduleapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scheduleapi.models.TarefaModel;
import com.example.scheduleapi.models.UserModel;

public interface TarefaRepository extends JpaRepository<TarefaModel, UUID>{
	@Query("SELECT t FROM TarefaModel t WHERE t.usuario.username =:username")
    public List<TarefaModel> findByUsername(@Param("username") String username);
}

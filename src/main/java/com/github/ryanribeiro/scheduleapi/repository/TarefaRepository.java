package com.github.ryanribeiro.scheduleapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.ryanribeiro.scheduleapi.entities.TarefaModel;

@Repository
public interface TarefaRepository extends JpaRepository<TarefaModel, UUID>{
	@Query("SELECT t FROM TarefaModel t WHERE t.usuario.username =:username")
    public List<TarefaModel> findByUsername(@Param("username") String username);
}

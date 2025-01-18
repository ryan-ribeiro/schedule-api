package com.example.scheduleapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduleapi.models.TarefaModel;

public interface TarefaRepository extends JpaRepository<TarefaModel, UUID>{

}

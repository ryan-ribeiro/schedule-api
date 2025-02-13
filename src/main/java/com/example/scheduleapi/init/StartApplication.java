package com.example.scheduleapi.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.scheduleapi.models.TarefaModel;
import com.example.scheduleapi.repositories.TarefaRepository;

@Component
public class StartApplication implements CommandLineRunner {
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public StartApplication(TarefaRepository tarefaRepository, PasswordEncoder passwordEncoder) {
        this.tarefaRepository = tarefaRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
	public void run(String... args) throws Exception {
		TarefaModel tarefa = tarefaRepository.findByUsername("admin");
		if(tarefa == null) {
			tarefa = new TarefaModel();
			tarefa.setNome("ADMIN");
			tarefa.setUsername("admin");
			tarefa.setPassword(passwordEncoder.encode("admin123"));
			tarefa.getRoles().add("MANAGERS");
			if (tarefa.getUri() == null) {
			    tarefa.setUri("algum_valor_padrao");
			}
			tarefaRepository.save(tarefa);
		}
		
		tarefa = tarefaRepository.findByUsername("user");
		if(tarefa == null) {
			tarefa = new TarefaModel();
			tarefa.setNome("USER");
			tarefa.setUsername("user");
			tarefa.setPassword(passwordEncoder.encode("user123"));
			tarefa.getRoles().add("USERS");
			if (tarefa.getUri() == null) {
			    tarefa.setUri("algum_valor_padrao");
			}
			tarefaRepository.save(tarefa);
		}
	}
}

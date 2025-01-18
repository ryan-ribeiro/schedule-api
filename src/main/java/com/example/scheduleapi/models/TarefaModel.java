package com.example.scheduleapi.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="TB_TAREFA")
public class TarefaModel extends RepresentationModel<TarefaModel >implements Serializable{
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "dt_inclusao")
	private Date dataInclusao;
	
	@Column(name = "dt_final")
	private Date dataFinal;
	
	@NotNull
	@Pattern(regexp = "^(https?|ftp)://[a-zA-Z0-9.-]+(:[0-9]+)?(/.*)?$", 
		     message = "Invalid URI")
	private String uri;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}

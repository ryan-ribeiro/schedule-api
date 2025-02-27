package com.github.ryanribeiro.scheduleapi.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.ryanribeiro.scheduleapi.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="TB_TAREFA")
public class TarefaModel extends RepresentationModel<TarefaModel >implements Serializable{
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@Column(name = "dt_inclusao", nullable = false, updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	private Date dataInclusao;
	
	@Column(name = "dt_final", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	private Date dataFinal;
	
	@Column(name = "tarefa_status")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne()
	@JoinColumn(name = "usuario_id", nullable = false, referencedColumnName = "id")
	@JsonBackReference
	private UserModel usuario;
	
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
	public UserModel getUsuario() {
		return usuario;
	}
	public void setUsuario(UserModel usuario) {
		this.usuario = usuario;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	

}

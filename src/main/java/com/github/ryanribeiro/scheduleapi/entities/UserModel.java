package com.github.ryanribeiro.scheduleapi.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_USER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
//@Setter
public class UserModel extends RepresentationModel<UserModel>{
	@Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;
	
	@Column(unique = true)
	private String username;
    
    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "role", nullable = false)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles;
    
<<<<<<< HEAD
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<TarefaModel> tarefas;
    
    @Column(name = "dt_inclusao", nullable = true, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date dataInclusao;
	
	@PrePersist
    protected void onCreate() {
		dataInclusao = new Date();
=======
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<TaskModel> tasks;
    
    @Column(name = "created_at", nullable = true, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date inclusionDate;
	
	@PrePersist
    protected void onCreate() {
		inclusionDate = new Date();
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
    }

	public void setId(UUID id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

<<<<<<< HEAD
	public void setTarefas(List<TarefaModel> tarefas) {
		this.tarefas = tarefas;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
=======
	public void setTarefas(List<TaskModel> tasks) {
		this.tasks = tasks;
	}

	public void setInclusionDate(Date inclusionDate) {
		this.inclusionDate = inclusionDate;
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
	}
	
	
}

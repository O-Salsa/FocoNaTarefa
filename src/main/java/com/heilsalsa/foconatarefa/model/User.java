package com.heilsalsa.foconatarefa.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name= "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
		private Role role; // Admin ou User 
	
	@OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
	private List<Task> task;

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

    public String getPassword() {
        return password;
}

public Role getRole() {
        return role;
}

public List<Task> getTask() {
        return task;
}

public void setUsername(String username) {
        this.username = username;
}

public void setPassword(String password) {
        this.password = password;
}

public void setRole(Role role) {
        this.role = role;
}

public void setTask(List<Task> task) {
        this.task = task;
}
	
	public enum Role {
		ADMIN, USER
	}

}

package com.ecommerce.userservice.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "users") // table name in MySQL
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Auto-increment primary key
    private Long id;

    @Column(nullable = false)
    // User full name
    private String name;

    @Column(nullable = false, unique = true)
    // Email must be unique
    private String email;

    @Column(nullable = false)
    // Password (later we will encrypt with BCrypt)
    private String password;

    @Column(nullable = false)
    private String role;   // USER or ADMIN

    
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
}

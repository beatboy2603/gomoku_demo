package com.example.gomoku_demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity 
@Table(name = "user")
@Data 
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotBlank(message = "Phai dien vao tai khoan")
	@Size(min=6, max=15, message = "Tai khoan phai chua tu 6 den 15 ki tu")
    @Column(nullable = false, unique = true)
    private String username;
	@NotBlank(message = "Phai co mat khau")
	@Size(min=8, message = "Mat khau phai tren 8 ki tu")
    private String password;
}

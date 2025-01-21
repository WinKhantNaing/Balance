package com.winkhant.balance.model.domain.entity;

import com.winkhant.balance.model.domain.form.SignUpForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {

	public User() {
	}

	public User(SignUpForm form) {
		this.name = form.getName();
		this.loginId = form.getLoginId();
		this.password = form.getPassword();
		this.active = true;
		this.role = Role.Member;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String loginId;
	@Column(nullable=false)
	private String password;
	private String phone;
	private String email;
	private Role role;
	private boolean active;
	
	public enum Role{
		Admin,
		Member
	}
}

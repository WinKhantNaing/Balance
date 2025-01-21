package com.winkhant.balance.model.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AccessLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public AccessLog() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String userName;
	private LocalDateTime accessTime;
	private String errorMessage; 	
	private Type type;
	
	public AccessLog(String userName, LocalDateTime accessTime, Type type) {
		super();
		this.userName = userName;
		this.accessTime = accessTime;
		this.type = type;
	}

	public AccessLog(String userName, LocalDateTime accessTime, String errorMessage, Type type) {
		super();
		this.userName = userName;
		this.accessTime = accessTime;
		this.errorMessage = errorMessage;
		this.type = type;
	}

	public enum Type{
		Login,
		Error,
		Logout
	}

}

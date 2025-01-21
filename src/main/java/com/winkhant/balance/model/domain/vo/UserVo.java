package com.winkhant.balance.model.domain.vo;

import com.winkhant.balance.model.domain.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class UserVo {

	public UserVo() {
	}
	
	public UserVo(User entity) {
		
		this.id = entity.getId();	
		this.email = entity.getEmail();
		this.loginId = entity.getLoginId();
		this.name = entity.getName();
		this.phone = entity.getPhone();
		this.status = entity.isActive();		
	}
	
	
	private int id;
	private String loginId;
	private String name;
	private boolean status;
	private String phone;
	private String email;
	
}

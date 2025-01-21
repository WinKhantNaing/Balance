package com.winkhant.balance.model.domain.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {
	
	@NotBlank(message = "Name must not be empty.")
	private String name;
	@NotBlank(message = "Login Id must not be empty.")
	private String loginId;
	@NotBlank(message = "Password must not be empty.")
	private String password;
	
}

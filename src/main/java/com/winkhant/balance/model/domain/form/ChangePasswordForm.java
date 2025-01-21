package com.winkhant.balance.model.domain.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordForm {

	private String loginId;
	private String oldPassword;
	private String newPassword;
}

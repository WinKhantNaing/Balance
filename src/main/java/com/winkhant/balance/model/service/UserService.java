package com.winkhant.balance.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.winkhant.balance.model.BalanceAppException;
import com.winkhant.balance.model.domain.entity.User;
import com.winkhant.balance.model.domain.entity.User.Role;
import com.winkhant.balance.model.domain.form.ChangePasswordForm;
import com.winkhant.balance.model.domain.form.SignUpForm;
import com.winkhant.balance.model.domain.vo.UserVo;
import com.winkhant.balance.model.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userrepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void signup(SignUpForm signUpForm) {
		signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
		User user = new User(signUpForm);
		userrepo.save(user);
		
	}

	public UserVo findByLoginId(String loginId) {
		return userrepo.findOneByLoginId(loginId).map(UserVo::new).orElseThrow();
	}

	@Transactional
	public void updateContact(String loginId, String phone, String email) {
		userrepo.findOneByLoginId(loginId)
		.ifPresent(user -> {
			user.setPhone(phone);
			user.setEmail(email);
		});
	}

	public List<UserVo> search(String name, String phone, Boolean status) {
		Specification<User> spec = (root,query,builder) -> builder.equal(root.get("role"), Role.Member);
		
		if(status != null) {
			spec = spec.and((root, query, builder) -> builder.equal(root.get("active"), status));
		}
		
		if(StringUtils.hasLength(name)) {
			spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("name")) , name.toLowerCase().concat("%")));
		}
		
		if(StringUtils.hasLength(phone)) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("phone") , phone.toLowerCase().concat("%")));
		}
		return userrepo.findAll(spec).stream().map(UserVo::new).toList();
	}

	@Transactional
	public void changeStatus(int id, boolean status) {
		userrepo.findById(id).ifPresent(user -> user.setActive(status));
		
	}

	@Transactional
	public void changePass(ChangePasswordForm form) {
		
		if(!StringUtils.hasLength(form.getOldPassword())) {
			throw new BalanceAppException("Old password must not be null");
		}
		
		if(!StringUtils.hasLength(form.getNewPassword())) {
			throw new BalanceAppException("New password must not be null");
		}
		
		if (form.getOldPassword().equals(form.getNewPassword())) {
			throw new BalanceAppException("Old password and new password must not be same");
		}
		
		var user = userrepo.findOneByLoginId(form.getLoginId()).orElseThrow();
		
		if (passwordEncoder.matches(form.getOldPassword(), form.getNewPassword())) {
			throw new BalanceAppException("New password must not be same with old password");
		}
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		
	}
	
	

}

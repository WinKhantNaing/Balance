package com.winkhant.balance.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.winkhant.balance.model.domain.entity.User;
import com.winkhant.balance.model.domain.entity.User.Role;
import com.winkhant.balance.model.repo.UserRepo;

import jakarta.transaction.Transactional;

@Component
public class AppUserInitializer {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	@EventListener(classes = ContextRefreshedEvent.class)
	public void initUser() {
		long users = repo.count();
		if(users == 0) {
			User user = new User();
			user.setLoginId("admin");
			user.setPassword(passwordEncoder.encode("admin"));
			user.setName("Admin");
			user.setRole(Role.Admin);
			user.setActive(true);
			repo.save(user);
		}
	}

}

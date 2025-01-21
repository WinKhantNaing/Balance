package com.winkhant.balance.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.winkhant.balance.model.domain.entity.AccessLog;
import com.winkhant.balance.model.domain.entity.AccessLog.Type;
import com.winkhant.balance.model.repo.AccessLogRepo;

import jakarta.transaction.Transactional;

@Component
public class BalanceAppAccessListener {
	
	@Autowired
	private AccessLogRepo repo;
	
	@EventListener
	@Transactional
	void onSuccess(AuthenticationSuccessEvent event) {
		
		var time = LocalDateTime.ofInstant(new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
		var userName = event.getAuthentication().getName();
		repo.save(new AccessLog(userName, time, Type.Login));
	}

	@EventListener
	@Transactional
	void onFailure(AbstractAuthenticationFailureEvent event) {
		
		var time = LocalDateTime.ofInstant(new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
		var userName = event.getAuthentication().getName();
		repo.save(new AccessLog(userName, time, event.getException().getMessage(), Type.Error));
		
	}
	
	@EventListener
	@Transactional
	void onSessionDestroyed(HttpSessionDestroyedEvent event) {
		
		event.getSecurityContexts().stream().findAny().ifPresent(
				(auth) -> {
					var time = LocalDateTime.ofInstant(new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
					var userName = auth.getAuthentication().getName();
					repo.save(new AccessLog(userName, time, Type.Logout));
		});
	}
}

package com.winkhant.balance.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.winkhant.balance.model.repo.UserRepo;

@Service
public class AppUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo repo;
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repo.findOneByLoginId(username).map(user -> User.withUsername(username)
				.password(user.getPassword())
				.authorities(AuthorityUtils.createAuthorityList(user.getRole().name()))
				.disabled(!user.isActive())
				.accountExpired(user.isActive())
				.build()).
				orElseThrow( () -> new UsernameNotFoundException("There is no such User: %s".formatted(username)));
		
	}
}

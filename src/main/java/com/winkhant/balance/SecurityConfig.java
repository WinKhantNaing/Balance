package com.winkhant.balance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.winkhant.balance.model.domain.entity.User.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/home", "/sign-in", "/sign-up").permitAll()
				.requestMatchers("/user/**").hasAnyAuthority(Role.Admin.name(), Role.Member.name())
				.requestMatchers("/admin/**").hasAuthority(Role.Admin.name())
				.anyRequest().authenticated());

		http.formLogin(form -> form.loginPage("/sign-in").loginProcessingUrl("/login").defaultSuccessUrl("/"));

		http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));

		http.exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"));

		return http.build();
	}
	
	@Bean
	AuthenticationEventPublisher publisher() {
		return new DefaultAuthenticationEventPublisher();
	}
	
	@Bean
	HttpSessionEventPublisher sessionPublisher() {
		return new HttpSessionEventPublisher();
	}

}

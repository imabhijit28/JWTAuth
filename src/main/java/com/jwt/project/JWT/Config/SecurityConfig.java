package com.jwt.project.JWT.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.project.JWT.Security.JwtAuthenticationEntryPoint;
import com.jwt.project.JWT.Security.JwtAuthenticationFilter;

public class SecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint point;
	
	@Autowired
	private JwtAuthenticationFilter filter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf->csrf.disable()).cors(cors->cors.disable())
		.authorizeHttpRequests(auth->auth.antMatchers("/home/**","/auth/**").permitAll().anyRequest().authenticated())
				.exceptionHandling(ex->ex.authenticationEntryPoint(point)).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//ChangeRequestMatchersToAntMatchers
		
		httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
}

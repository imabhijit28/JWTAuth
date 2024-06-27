package com.jwt.project.JWT.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.project.JWT.Models.JwtRequest;
import com.jwt.project.JWT.Models.JwtResponse;
import com.jwt.project.JWT.Security.JwtHelper;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JwtHelper helper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
		this.doAuthenticate(request.getEmail(), request.getPassword());
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);
		JwtResponse response = JwtResponse.builder().jwtToken(token).userName(userDetails.getUsername()).build();
		return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
	}
	
	private void doAuthenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
		}catch (BadCredentialsException e) {
			throw new RuntimeException("Invalid Username Or Password !!");
		}
	}
}

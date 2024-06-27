package com.jwt.project.JWT.Controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.project.JWT.Models.User;
import com.jwt.project.JWT.Service.UserService;

@RestController
@RequestMapping("/home")
public class HomeController {

	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public List<User> getUser() {
		
		return this.userService.getUsers();
	}
	
	@GetMapping("/getCurrentUser")
	public String getLoggedInUser(Principal principal) {
		logger.debug("Getting UserName");
		return principal.getName();
	}
}

package com.jwt.project.JWT.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jwt.project.JWT.Models.User;

@Service
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private List<User> store = new ArrayList<>();
	
	public UserService() {
		store.add(new User(UUID.randomUUID().toString(),"AKS","a@gmail.com"));
		store.add(new User(UUID.randomUUID().toString(),"TKS","t@gmail.com"));
		store.add(new User(UUID.randomUUID().toString(),"PKS","p@gmail.com"));
		store.add(new User(UUID.randomUUID().toString(),"GKS","g@gmail.com"));
	}
	
	public List<User> getUsers(){
		return store;
	}
}

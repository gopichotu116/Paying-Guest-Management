package com.org.payingGuest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.payingGuest.entity.User;
import com.org.payingGuest.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;

	public void save(User user) {
		userRepo.save(user);
	}

	public Integer checkEmailAndPassword(String email, String password) {
		return userRepo.findIdByEmailAndPassword(email, password);
	}

	public List<User> getAll() {
		return userRepo.findAll();
	}
	
}

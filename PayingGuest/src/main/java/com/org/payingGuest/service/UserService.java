package com.org.payingGuest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.payingGuest.entity.User;
import com.org.payingGuest.repository.UserRepository;

import jakarta.transaction.Transactional;

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

	public String getNameById(Integer userId) {
		return userRepo.findNameById(userId);
	}

	public User getUserById(Integer id) {
		return userRepo.findById(id).get();
	}

	public void updateUser(User user) {
		userRepo.save(user);
	}

	public byte[] getProfileImage(Integer userId) {
        Optional<User> optionalCustomer =userRepo.findById(userId);
        if (optionalCustomer.isPresent()) {
            User user = optionalCustomer.get();
            return user.getProfilePicture();
        } else {
            return null;
        }
    }

	@Transactional
	public void updateImage(Integer id, byte[] profilePicture) {
		userRepo.updateImageById(id, profilePicture);
	}

	public User getIdByEmail(String email) {
		return userRepo.findIdByEmail(email);
	}
	
}

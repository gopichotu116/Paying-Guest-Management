package com.org.payingGuest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.payingGuest.entity.Owner;
import com.org.payingGuest.repository.OwnerRepository;

@Service
public class OwnerService {
	
	@Autowired
	private OwnerRepository ownerRepo;

	public void save(Owner owner) {
		ownerRepo.save(owner);
	}

	public Integer checkEmailAndPassword(String email, String password) {
		return ownerRepo.findIdByEmailAndPassword(email, password);
	}

	public Owner getById(Integer i) {
		return ownerRepo.findById(i).get();
	}

	public List<Owner> getAll() {
		return ownerRepo.findAll();
	}

}

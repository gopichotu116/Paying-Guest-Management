package com.org.payingGuest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.payingGuest.entity.Owner;
import com.org.payingGuest.repository.OwnerRepository;

import jakarta.transaction.Transactional;

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
	
	public String getNameById(Integer ownerId) {
		return ownerRepo.findNameById(ownerId);
	}
	
	public void updateOwner(Owner owner) {
		ownerRepo.save(owner);
	}
	
	public byte[] getProfileImage(Integer ownerId) {
        Optional<Owner> optionalOwner =ownerRepo.findById(ownerId);
        if (optionalOwner.isPresent()) {
            Owner owner = optionalOwner.get();
            return owner.getProfilePicture();
        } else {
            return null;
        }
    }

	@Transactional
	public void updateImage(Integer id, byte[] profilePicture) {
		ownerRepo.updateImageById(id, profilePicture);
	}

	public Owner getIdByEmail(String email) {
		return ownerRepo.findIdByEmail(email);
	}

}

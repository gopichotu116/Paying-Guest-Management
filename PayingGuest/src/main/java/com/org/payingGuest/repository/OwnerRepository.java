package com.org.payingGuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.Owner;

import jakarta.transaction.Transactional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer>{

	@Query("SELECT o.id FROM Owner o WHERE o.email=:email AND o.password=:password")
	Integer findIdByEmailAndPassword(String email, String password);
	
	@Query("SELECT o.name FROM Owner o WHERE o.id=:ownerId")
	String findNameById(Integer ownerId);
	
	@Modifying
	@Transactional
	@Query("UPDATE Owner o SET o.profilePicture=:profilePicture WHERE id=:id")
	void updateImageById(Integer id, byte[] profilePicture);
	
	@Query("SELECT o FROM Owner o WHERE o.email=:email")
	Owner findIdByEmail(String email);
}

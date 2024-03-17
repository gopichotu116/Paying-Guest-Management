package com.org.payingGuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer>{

	@Query("SELECT o.id FROM Owner o WHERE o.email=:email AND o.password=:password")
	Integer findIdByEmailAndPassword(String email, String password);

}

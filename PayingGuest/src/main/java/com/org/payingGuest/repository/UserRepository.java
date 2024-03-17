package com.org.payingGuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("SELECT u.id FROM User u WHERE u.email=:email AND u.password=:password")
	Integer findIdByEmailAndPassword(String email, String password);
	
//	User findByEmailAndPassword(String email, String pass);
}

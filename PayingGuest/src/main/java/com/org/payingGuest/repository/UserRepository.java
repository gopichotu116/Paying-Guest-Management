package com.org.payingGuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u.id FROM User u WHERE u.email=:email AND u.password=:password")
	Integer findIdByEmailAndPassword(String email, String password);

	@Query("SELECT u.name FROM User u WHERE u.id=:userId")
	String findNameById(Integer userId);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.profilePicture=:profilePicture WHERE id=:id")
	void updateImageById(Integer id, byte[] profilePicture);

	@Query("SELECT u FROM User u WHERE u.email=:email")
	User findIdByEmail(String email);

}

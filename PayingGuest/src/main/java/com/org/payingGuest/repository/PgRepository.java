package com.org.payingGuest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.Pg;

@Repository
public interface PgRepository extends JpaRepository<Pg,Integer>{

	@Query("SELECT p FROM Pg p WHERE p.owner.id=:ownerId")
	List<Pg> findPgsByOwnerId(Integer ownerId);

	@Query("SELECT p FROM Pg p WHERE p.location=:location AND p.area=:area")
	List<Pg> findPgsByLocationAndArea(String location, String area);

}

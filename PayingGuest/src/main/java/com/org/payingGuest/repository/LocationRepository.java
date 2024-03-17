package com.org.payingGuest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query("SELECT l.id FROM Location l WHERE l.name=:location")
	Integer findIdByName(String location);

	@Query("SELECT l.name FROM Location l ORDER BY l.name")
	List<String> findAllNames();

}

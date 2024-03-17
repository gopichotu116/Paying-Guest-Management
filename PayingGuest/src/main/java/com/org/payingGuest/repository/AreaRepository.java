package com.org.payingGuest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.payingGuest.entity.Area;
import com.org.payingGuest.entity.Pg;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	@Query("SELECT a.id FROM Area a WHERE a.name=:area")
	Integer findIdByName(String area);

	@Query("SELECT a.name FROM Area a WHERE a.location.id=:locId ORDER BY a.name")
	List<String> findNamesByLocationId(Integer locId);

	
	@Query("SELECT a from Area a WHERE a.location.id=:id")
	List<Area> findAreasByLocationId(Integer id);

//	List<Area> findByLocationId(Integer locationId);

}

package com.org.payingGuest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.payingGuest.entity.Location;
import com.org.payingGuest.repository.LocationRepository;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepository locationRepo;

	public Integer getIdByName(String location) {
		return locationRepo.findIdByName(location);
	}

	public void save(Location loc) {
		locationRepo.save(loc);
	}

	public Location getById(Integer locId) {
		return locationRepo.findById(locId).get();
	}

	public List<String> getAllNames() {
		return locationRepo.findAllNames();
	}

}

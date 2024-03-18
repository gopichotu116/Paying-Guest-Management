package com.org.payingGuest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.payingGuest.entity.Area;
import com.org.payingGuest.repository.AreaRepository;
import com.org.payingGuest.repository.LocationRepository;

@Service
public class AreaService {
	
	@Autowired
	private AreaRepository areaRepo;
	
	@Autowired
	private LocationRepository locationRepo;

	public Integer getIdByName(String area) {
		return areaRepo.findIdByName(area);
	}

	public void save(Area area) {
		areaRepo.save(area);
	}

	public List<String> getNamesByLocationId(Integer locId) {
		return areaRepo.findNamesByLocationId(locId);
	}

	public List<Area> getAll(String loc) {
		return areaRepo.findAreasByLocationId(locationRepo.findIdByName(loc));
	}


}

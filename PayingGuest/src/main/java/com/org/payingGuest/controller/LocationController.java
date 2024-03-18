package com.org.payingGuest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.payingGuest.entity.Area;
import com.org.payingGuest.entity.Location;
import com.org.payingGuest.repository.LocationRepository;
import com.org.payingGuest.service.AreaService;

@Controller
public class LocationController {

	@Autowired
	private LocationRepository locationRepo;

	@Autowired
	private AreaService areaService;

	@GetMapping("/loc")
	@ResponseBody
	public List<Location> getList() {
		return locationRepo.findAll();
	}

	@GetMapping("/areas/{loc}")
	@ResponseBody
	public List<Area> getDistricts(@PathVariable String loc) {
		return areaService.getAll(loc);
	}

}

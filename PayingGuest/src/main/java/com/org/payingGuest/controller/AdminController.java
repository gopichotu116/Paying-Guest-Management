package com.org.payingGuest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.org.payingGuest.entity.Area;
import com.org.payingGuest.entity.Location;
import com.org.payingGuest.service.AreaService;
import com.org.payingGuest.service.LocationService;

@Controller
public class AdminController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private AreaService areaService;
	
	/** ------------------------ Admin Login--------------------------------*/
	
	@GetMapping("/adminLogin")
	public String adminLogin() {
		return "admin/adminLogin";
	}
	
	@PostMapping("/adminLogin")
	public String checkAdminLogin(@RequestParam("email") String email,
			@RequestParam("password") String password, Model model) {
		if((email.equals("admin@gmail.com") && password.equals("123"))) {
			return "redirect:/adminHome";
		}
		model.addAttribute("errorMsg", "Incorrect Password or Email");
		return "admin/adminLogin";
	}
	
	/** ------------------------ Admin Home--------------------------------*/
	
	@GetMapping("/adminHome")
	public String adminHomePage() {
		return "admin/adminHome";
	}

	/** ------------------------ Add Location--------------------------------*/
	
	@GetMapping("/addLocation")
	public String addLocation() {
		return "admin/addLocation";
	}

	@PostMapping("/addLocation")
	public String addLocationForm(@RequestParam("location") String location, 
			@RequestParam("area") String area, Model model) {
		
		Integer locId = locationService.getIdByName(location);
		if (locId == null) {
			Location loc = new Location(location);
			locationService.save(loc);
			locId=loc.getId();
			
		}
		Location Location = locationService.getById(locId);
		
		Integer areaId = areaService.getIdByName(area);
		if (areaId == null) {
			Area Area = new Area(area, Location);
			areaService.save(Area);
			model.addAttribute("successMsg", "LOCATION IS ADDED SUCCESSFULLY");
			return "admin/addLocation";
		} else {
			model.addAttribute("errorMsg", "AREA IS ALREADY ADDED");
			return "admin/addLocation";
		}
	}
	
	/** ------------------------ Locations List with Pagination--------------------------------*/
	
	@GetMapping("/locationsList")
	public String locationsList(Model model) {
		List<String> list = locationService.getAllNames();
		model.addAttribute("locations", list);
		return "admin/searchLocation";
	}
	
	@GetMapping("/areasList")
	public String displayAreasList(@RequestParam("location") String location,
	                               @RequestParam(name = "page", defaultValue = "1") int page,
	                               Model model) {
	    // Get areas list based on the selected location
		Integer locId = locationService.getIdByName(location);
		List<String> areasList = areaService.getNamesByLocationId(locId);

	    // Pagination logic
	    int pageSize = 7; // Number of areas per page
	    int totalPages = (int) Math.ceil((double) areasList.size() / pageSize);
	    int startIndex = (page - 1) * pageSize;
	    int size=areasList.size();
	    int endIndex = Math.min(startIndex + pageSize, size);
	    List<String> paginatedAreas = areasList.subList(startIndex, endIndex);

	    // Pass data to the Thymeleaf template
	    model.addAttribute("location", location);
	    model.addAttribute("count", size);
	    model.addAttribute("areas", paginatedAreas);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);

	    return "admin/areasList"; // Thymeleaf template name
	}
	
	
}

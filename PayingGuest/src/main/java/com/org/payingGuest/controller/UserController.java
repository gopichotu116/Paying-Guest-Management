package com.org.payingGuest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.org.payingGuest.entity.User;
import com.org.payingGuest.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * ----------------------------------- User SignUp
	 * ------------------------------------
	 */

	@GetMapping("/userSignup")
	public String userSignup() {
		return "user/userSignup";
	}

	@PostMapping("/userSignup")
	public String checkUserSingup(@RequestParam("name") String name, @RequestParam String email,
			@RequestParam("password") String password, @RequestParam("phno") String phno, Model model) {
		User user = new User(name, email, password, phno);
		userService.save(user);
		model.addAttribute("successMsg", "YOUR ACCOUNT IS CREATED SUCCESSFULLY");
		return "user/userLogin";
	}

	/**
	 * ----------------------------------- User Login
	 * ------------------------------------
	 */

	@GetMapping("/userLogin")
	public String userLogin() {
		return "user/userLogin";
	}

	@PostMapping("/userLogin")
	public String checkUserLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			Model model) {
		Integer userId = userService.checkEmailAndPassword(email, password);
		if (userId == null) {
			model.addAttribute("errorMsg", "INVALID EMAIL OR PASSWORD");
			return "user/userLogin";
		}
		return "user/userHome";
	}

	/**
	 * ----------------------------------- User Home
	 * ------------------------------------
	 */

	@GetMapping("/userHome")
	public String userHomePage() {
		return "user/userHome";
	}

	/** ------------------------ Users List -------------------------------- */

	@GetMapping("/usersList")
	public String usersList(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {

		List<User> usersList = userService.getAll();

		int pageSize = 5; // Number of areas per page
		int totalPages = (int) Math.ceil((double) usersList.size() / pageSize);
		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, usersList.size());
		List<User> paginatedAreas = usersList.subList(startIndex, endIndex);

		model.addAttribute("users", paginatedAreas);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);

		return "admin/usersList";
	}

	/**
	 * ----------------------------------- Search PG
	 * ------------------------------------
	 */

	@GetMapping("/searchPg")
	public String findPg() {
		return "user/searchPg";
	}

	@PostMapping("/searchPg")
	public String listOfPg(@RequestParam("location") String location, @RequestParam("area") String area) {
		return "user/pgsList";
	}

}

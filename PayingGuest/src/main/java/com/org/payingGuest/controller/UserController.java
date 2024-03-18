package com.org.payingGuest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.payingGuest.entity.Pg;
import com.org.payingGuest.entity.User;
import com.org.payingGuest.service.PgService;
import com.org.payingGuest.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PgService pgService;

	private String name;

	private Integer id;

	private byte[] profileImage;

	/** --------------------------- Model Attributes------------------------- */

	@ModelAttribute("userName")
	public String addNameAttribute() {
		String userName = name;
		return userName;
	}

	@ModelAttribute("userId")
	public Integer addIdAttribute() {
		Integer userId = id;
		return userId;
	}

	@ModelAttribute("userProfileImage")
	public String addImageAttribute() {
		return profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "";
	}

	/** --------------------------- User SignUp--------------------------- */

	@GetMapping("/userSignup")
	public String userSignup() {
		return "user/userSignup";
	}

	@PostMapping("/userSignup")
	public String checkUserSingup(@RequestParam("name") String name, @RequestParam String email,
			@RequestParam("password") String password, @RequestParam("phno") String phno, Model model) {
		if (passValidation(password)) {
			User user = new User(name, email, password, phno);
			userService.save(user);
			model.addAttribute("successMsg", "YOUR ACCOUNT IS CREATED SUCCESSFULLY");
			return "user/userLogin";
		}
		model.addAttribute("password", "PASSWORD REQUIREMENTS NOT MATCHED");
		return "user/userSignup";
	}

	/** --------------------------- User Login--------------------------- */

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
		name = userService.getNameById(userId);
		id = userId;
		model.addAttribute("userName", name);
		profileImage = userService.getProfileImage(id);
		model.addAttribute("userProfileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		return "user/userHome";
	}

	/** --------------------------- User Home--------------------------- */

	@GetMapping("/userHome")
	public String userHomePage() {
		return "user/userHome";
	}

	/** ------------------------ Users List ------------------------------ */

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

	/** ------------------------ Search PG ------------------------------ */

	@GetMapping("/searchPg")
	public String findPg() {
		return "user/searchPg";
	}

	@PostMapping("/searchPg")
	public String listOfPg(@RequestParam("location") String location, @RequestParam("area") String area, Model model) {
		List<Pg> pgs = pgService.getPgsByLocationAndArea(location, area);
		model.addAttribute("pgs", pgs);
		return "user/pgsList";
	}

	/** ------------------------ Search PG ------------------------------ */

	@GetMapping("/viewPg/{id}")
	public String viewPg(@PathVariable Integer id, Model model) {

		Pg pg = pgService.getById(id);

		byte[] boardImage = pg.getBoardImage();
		model.addAttribute("boardImage", boardImage != null ? Base64.getEncoder().encodeToString(boardImage) : "");

		byte[] rentCard = pg.getRentCard();
		model.addAttribute("rentCard", rentCard != null ? Base64.getEncoder().encodeToString(rentCard) : "");

		byte[] rulesImage = pg.getRulesImage();
		model.addAttribute("rulesImage", rulesImage != null ? Base64.getEncoder().encodeToString(rulesImage) : "");

		List<byte[]> outsideImages = pg.getOutsideImages();
		List<String> outside = new ArrayList<>();

		if (outsideImages != null) {
			for (byte[] image : outsideImages) {
				outside.add(Base64.getEncoder().encodeToString(image));
			}
		}

		List<byte[]> insideImages = pg.getInsideImages();
		List<String> inside = new ArrayList<>();
		if (insideImages != null) {
			for (byte[] image : insideImages) {
				inside.add(Base64.getEncoder().encodeToString(image));
			}
		}

		model.addAttribute("outsideImages", outside);
		model.addAttribute("insideImages", inside);

		model.addAttribute("pg", pg);
		return "user/viewPg";
	}

	/** ------------------------------- User Profile------------------------ */

	@GetMapping("/userProfile")
	public String userProfile(Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("userProfile", user);
		profileImage = userService.getProfileImage(user.getId());
		model.addAttribute("userProfileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		return "user/userProfile";
	}

	@GetMapping("/editUserProfile/{id}")
	public String editProfile(@PathVariable("id") Integer id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("userProfile", user);
		return "/user/editProfile";
	}

	@PostMapping("/saveUser")
	public String updateUser(@ModelAttribute User user, Model model) {
		model.addAttribute("userProfile", user);
		if (passValidation(user.getPassword())) {
			byte[] image = userService.getProfileImage(id);
			user.setProfilePicture(image);
			userService.updateUser(user);
			return "redirect:/userProfile";
		}
		model.addAttribute("password", "PASSWORD REQUIREMENTS NOT MATCHED");
		return "user/editProfile";
	}

	@PostMapping("/saveUserProfileImage")
	public String updateImage(@RequestParam("file") MultipartFile file) throws IOException {
		User user = userService.getUserById(id);
		user.setProfilePicture(file.getBytes());
		byte[] profilePicture = user.getProfilePicture();
		userService.updateImage(id, profilePicture);
		return "redirect:/userProfile";
	}

	@GetMapping("/editUserProfileImage")
	public String updateProfileImage() {
		return "user/editProfileImage";
	}

	@GetMapping("/userForgotPass")
	public String forgotPassword() {
		return "user/forgotPassword";
	}

	@PostMapping("/userForgotPass")
	public String getPassword(@RequestParam("email") String email, Model model) {
		User user = userService.getIdByEmail(email);
		if (user == null) {
			model.addAttribute("errorMsg", "INVALID EMAIL ADDRESS");
			return "user/forgotPassword";
		}
		model.addAttribute("successMsg", user.getPassword());
		return "user/forgotPassword";
	}

	/** ------------------------- Password Validation ----------------------- */

	public boolean passValidation(String pass) {
		String regexp = "(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{5,16}";
		Matcher m = Pattern.compile(regexp).matcher(pass);
		return m.matches();

	}
}

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

import com.org.payingGuest.entity.Owner;
import com.org.payingGuest.entity.Pg;
import com.org.payingGuest.service.OwnerService;
import com.org.payingGuest.service.PgService;

@Controller
public class OwnerController {

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private PgService pgService;

	private String name;

	private Integer id;

	private byte[] profileImage;

	@ModelAttribute("ownerName")
	public String addNameAttribute() {
		String ownerName = name;
		return ownerName;
	}

	@ModelAttribute("ownerId")
	public Integer addIdAttribute() {
		Integer ownerId = id;
		return ownerId;
	}

	@ModelAttribute("ownerProfileImage")
	public String addImageAttribute() {
		return profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "";
	}

	/** ----------------------- Owner Signup ---------------------------- */

	@GetMapping("/ownerSignup")
	public String ownerSignup() {
		return "owner/ownerSignup";
	}

	@PostMapping("/ownerSignup")
	public String signup(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("phno") String phno, Model model) {
		if (passValidation(password)) {
			Owner owner = new Owner(name, email, password, phno);
			ownerService.save(owner);
			model.addAttribute("successMsg", "YOUR ACCOUNT IS CREATED SUCCESSFULLY");
			return "owner/ownerLogin";
		}
		model.addAttribute("password", "PASSWORD REQUIREMENTS NOT MATCHED");
		return "owner/ownerSignup";
	}

	/** ----------------------- Owner Login ---------------------------- */

	@GetMapping("/ownerLogin")
	public String ownerLogin() {
		return "owner/ownerLogin";
	}

	@PostMapping("/ownerLogin")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
		Integer ownerId = ownerService.checkEmailAndPassword(email, password);
		if (ownerId == null) {
			model.addAttribute("errorMsg", "INVALID EMAIL OR PASSWORD");
			return "owner/ownerLogin";
		}
		name = ownerService.getNameById(ownerId);
		id = ownerId;
		model.addAttribute("ownerName", name);
		profileImage = ownerService.getProfileImage(id);
		model.addAttribute("ownerProfileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		return "owner/ownerHome";
	}

	/** ----------------------- Owner Home ---------------------------- */

	@GetMapping("/ownerHome")
	public String ownerHomePage() {
		return "owner/ownerHome";
	}

	/** ----------------------- Owner List ---------------------------- */

	@GetMapping("/ownersList")
	public String ownersList(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {

		List<Owner> usersList = ownerService.getAll();

		int pageSize = 5; // Number of areas per page
		int totalPages = (int) Math.ceil((double) usersList.size() / pageSize);
		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, usersList.size());
		List<Owner> paginatedAreas = usersList.subList(startIndex, endIndex);

		model.addAttribute("owners", paginatedAreas);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);

		return "admin/ownersList";
	}

	/** ----------------------- PG ---------------------------- */

	@GetMapping("/addPg")
	public String addPg() {
		return "owner/addPg";
	}

	@PostMapping("/addPg")
	public String uploadPg(@RequestParam("name") String name, @RequestParam("location") String location,
			@RequestParam("area") String area, @RequestParam("outsideImages") List<MultipartFile> outsideImages,
			@RequestParam("insideImages") List<MultipartFile> insideImages,
			@RequestParam("rulesImage") MultipartFile rulesImage, @RequestParam("rentCard") MultipartFile rentCard,
			@RequestParam("boardImage") MultipartFile boardImage, @RequestParam("phno") String phno,
			@RequestParam("maps") String maps, @RequestParam("address") String address, Model model)
			throws IOException {

		Owner owner = ownerService.getById(id);
		pgService.savePg(name, location, area, outsideImages, insideImages, rulesImage, rentCard, boardImage, phno,
				maps, address, owner);
		model.addAttribute("successMsg", "PG IS ADDED SUCCESSFULLY");
		return "owner/addPg";
	}

	@GetMapping("/myPgs")
	public String listOfMyPgs(Model model) {
		Integer ownerId = 1;
		List<Pg> pgsList = pgService.getPgsByOwnerId(ownerId);
		if (pgsList == null) {
			model.addAttribute("errorMsg", "NO PGS ARE AVAILABLE PLEASE ADD YOUR PG");
			return "owner/myPgsList";
		}
		model.addAttribute("myPgsList", pgsList);
		return "owner/myPgsList";
	}

	@GetMapping("/pg/{id}")
	public String getPg(@PathVariable("id") Integer id, Model model) {
		Pg pg = pgService.getPg(id);

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

		return "owner/viewPg";
	}

	/** -------------------- Owner Profile------------------------------- */

	@GetMapping("/ownerProfile")
	public String ownerProfile(Model model) {
		Owner owner = ownerService.getById(id);
		model.addAttribute("ownerProfile", owner);
		profileImage = ownerService.getProfileImage(owner.getId());
		model.addAttribute("ownerProfileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		return "owner/ownerProfile";
	}

	@GetMapping("/editOwnerProfile/{id}")
	public String editProfile(@PathVariable("id") Integer id, Model model) {
		Owner owner = ownerService.getById(id);
		model.addAttribute("ownerProfile", owner);
		return "owner/editProfile";
	}

	@PostMapping("/saveOwner")
	public String updateOwner(@ModelAttribute Owner owner, Model model) {
		model.addAttribute("ownerProfile", owner);
		if (passValidation(owner.getPassword())) {
			byte[] image = ownerService.getProfileImage(id);
			owner.setProfilePicture(image);
			ownerService.updateOwner(owner);
			return "redirect:/ownerProfile";
		}
		model.addAttribute("password", "PASSWORD REQUIREMENTS NOT MATCHED");
		return "owner/editProfile";
	}

	@PostMapping("/saveOwnerProfileImage")
	public String updateImage(@RequestParam("file") MultipartFile file) throws IOException {
		Owner owner = ownerService.getById(id);
		owner.setProfilePicture(file.getBytes());
		byte[] profilePicture = owner.getProfilePicture();
		ownerService.updateImage(id, profilePicture);
		return "redirect:/ownerProfile";
	}

	@GetMapping("/editOwnerProfileImage")
	public String updateProfileImage() {
		return "owner/editProfileImage";
	}

	@GetMapping("/ownerForgotPass")
	public String forgotPassword() {
		return "owner/forgotPassword";
	}

	@PostMapping("/ownerForgotPass")
	public String getPassword(@RequestParam("email") String email, Model model) {
		Owner owner = ownerService.getIdByEmail(email);
		if (owner == null) {
			model.addAttribute("errorMsg", "INVALID EMAIL ADDRESS");
			return "owner/forgotPassword";
		}
		model.addAttribute("successMsg", owner.getPassword());
		return "owner/forgotPassword";
	}

	/** -------------------- Password Validation------------------------------- */

	public boolean passValidation(String pass) {
		String regexp = "(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{5,16}";
		Matcher m = Pattern.compile(regexp).matcher(pass);
		return m.matches();

	}
}

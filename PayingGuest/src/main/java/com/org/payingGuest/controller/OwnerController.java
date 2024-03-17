package com.org.payingGuest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.payingGuest.entity.Owner;
import com.org.payingGuest.entity.Pg;
import com.org.payingGuest.service.AreaService;
import com.org.payingGuest.service.OwnerService;
import com.org.payingGuest.service.PgService;

@Controller
public class OwnerController {

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private PgService pgService;

	/** ----------------------------------------- Owner Signup --------------------------------------- */

	@GetMapping("/ownerSignup")
	public String ownerSignup() {
		return "owner/ownerSignup";
	}

	@PostMapping("/ownerSignup")
	public String signup(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("phno") String phno, Model model) {
		Owner owner = new Owner(name, email, password, phno);
		ownerService.save(owner);
		model.addAttribute("successMsg", "YOUR ACCOUNT IS CREATED SUCCESSFULLY");
		return "owner/ownerLogin";
	}

	/** ----------------------------------------- Owner Login --------------------------------------- */

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
		return "redirect:/ownerHome";
	}

	/** -------------------------------------- Owner Home --------------------------------------- */

	@GetMapping("/ownerHome")
	public String ownerHomePage() {
		return "owner/ownerHome";
	}

	/** -------------------------------------- Owners List ----------------------------------------- */

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

	/** ----------------------------------------- PG --------------------------------------- */

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
		Integer ownerId = 1;
		Owner owner = ownerService.getById(ownerId);
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
}

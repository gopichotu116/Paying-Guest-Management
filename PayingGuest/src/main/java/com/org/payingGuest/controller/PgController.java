package com.org.payingGuest.controller;

import java.io.IOException;
import java.util.List;

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
public class PgController {

	@Autowired
	private PgService pgService;

	@Autowired
	private OwnerService ownerService;

	/** -------------------- Delete PG ------------------------ */

	@GetMapping("/deletePg/{id}")
	public String deletePg(@PathVariable("id") Integer id, Model model) {
		pgService.deletePgById(id);
		return "owner/deletePg";
	}
	
	/** -------------------- Edit PG ------------------------ */

	@GetMapping("/editPg/{id}")
	public String editPg(@PathVariable("id") Integer id, Model model) {
		Pg pg = pgService.getById(id);
		model.addAttribute("pg", pg);
		return "owner/editPg";
	}

	/** -------------------- Update PG ------------------------ */
	
	@PostMapping("/savePg")
	public String updatePg(@ModelAttribute Pg pg, @RequestParam("outsideImages") List<MultipartFile> outsideImages,
			@RequestParam("insideImages") List<MultipartFile> insideImages,
			@RequestParam("rulesImage") MultipartFile rulesImage, @RequestParam("rentCard") MultipartFile rentCard,
			@RequestParam("boardImage") MultipartFile boardImage, Model model) throws IOException {

		// Set the images to the Pg entity
		pg.setOutsideImages(pgService.convertImages(outsideImages));
		pg.setInsideImages(pgService.convertImages(insideImages));
		pg.setRulesImage(rulesImage.getBytes());
		pg.setRentCard(rentCard.getBytes());
		pg.setBoardImage(boardImage.getBytes());

		Owner owner = ownerService.getById(1);
		pg.setOwner(owner);
		pgService.updatePg(pg);
		model.addAttribute("successMsg", "PG IS UPDATED SUCCESSFULLY");
		// Redirect to a confirmation page or another URL
		return "owner/myPgsList"; // Replace with the desired UR\
	}

}

package com.org.payingGuest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.org.payingGuest.entity.Pg;
import com.org.payingGuest.service.PgService;

@Controller
public class PgController {
	
	@Autowired
	private PgService pgService;
	
	@GetMapping("/deletePg/{id}")
	public String deletePg(@PathVariable("id") Integer id, Model model) {
		pgService.deletePgById(id);
		return "owner/deletePg";
	}
	
	@GetMapping("/editPg/{id}")
	public String editPg(@PathVariable("id") Integer id, Model model) {
		Pg pg = pgService.getById(id);
		model.addAttribute("pg", pg);
		return "owner/editPg";
	}
	
}

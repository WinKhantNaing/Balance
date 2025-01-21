package com.winkhant.balance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.winkhant.balance.model.service.UserService;

@Controller
@RequestMapping("admin/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping()
	public String search(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String phone, 
			@RequestParam(required = false) Boolean status,
			Model m
			) {
		
		var list = userService.search(name,phone,status);
		m.addAttribute("list",list);
		
		return "users";
	}
	
	@PostMapping("status")
	public String changeUserStatus(
			@RequestParam int id,
			@RequestParam boolean status
			) {
		userService.changeStatus(id, !status);
		
		return "redirect:/admin/users";
	}
	

}

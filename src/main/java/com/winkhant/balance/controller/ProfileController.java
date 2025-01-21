package com.winkhant.balance.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.winkhant.balance.controller.utils.Pagination;
import com.winkhant.balance.controller.utils.Pagination.Builder;
import com.winkhant.balance.model.domain.entity.AccessLog;
import com.winkhant.balance.model.domain.vo.UserVo;
import com.winkhant.balance.model.service.AccessLogService;
import com.winkhant.balance.model.service.UserService;

@Controller
@RequestMapping("user/profile")
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccessLogService accessLogService;
	
	
	
	@GetMapping
	public String showProfile(Model m,@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		UserVo userVo = userService.findByLoginId(loginId);
		m.addAttribute("user",userVo);
		
		Page<AccessLog> accessLogs = accessLogService.search(loginId, page, size);
		m.addAttribute("list",accessLogs.getContent());
		Pagination pagination = Pagination.builder("/user/profile").page(accessLogs).build();
		m.addAttribute("pagination",pagination);
		return "profile";
	}
	
	@PostMapping("contact")
	public String saveContact(@RequestParam(required=false) String phone,@RequestParam(required=false) String email) {
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		userService.updateContact(loginId,phone,email);
		return "redirect:/user/profile";
	}

}

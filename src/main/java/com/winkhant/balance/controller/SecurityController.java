package com.winkhant.balance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.winkhant.balance.model.domain.entity.User.Role;
import com.winkhant.balance.model.domain.form.ChangePasswordForm;
import com.winkhant.balance.model.domain.form.SignUpForm;
import com.winkhant.balance.model.service.UserService;

import jakarta.validation.Valid;

@Controller
public class SecurityController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index(Model m,RedirectAttributes redirect) {
		String message = (String) m.getAttribute("message");
		redirect.addFlashAttribute("message",message);
	    var auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.getAuthorities().stream().anyMatch(
	            a -> a.getAuthority().equals(Role.Admin.name()) || a.getAuthority().equals(Role.Member.name()))) {
	        return "redirect:/user/home";
	    }
	    return "sign-in";
	}


	@GetMapping("sign-up")
	public void loadSignUp() {
	}

	@PostMapping("sign-up")
	public String signUp(@ModelAttribute("form") @Valid SignUpForm signUpForm, BindingResult result) {
		if (result.hasErrors()) {
			System.out.println("has error");
			return "sign-up";
		}
		userService.signup(signUpForm);
		return "redirect:/";
	}

	@PostMapping("user/change-password") 
	public String changepassword(@ModelAttribute ChangePasswordForm form, RedirectAttributes redirect) {
		userService.changePass(form);
		redirect.addFlashAttribute("message", "Password changed successfully");
		System.out.println("changed");
		return "redirect:/";
	}

	@ModelAttribute("form")
	SignUpForm signUpForm() {
		return new SignUpForm();
	}

}

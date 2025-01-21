package com.winkhant.balance.controller.utils;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.winkhant.balance.model.BalanceAppException;

@ControllerAdvice
public class BalanceAppExceptionHandler {
	
	@ExceptionHandler(value = BalanceAppException.class)
	String handler(BalanceAppException e,RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", e.getMessage());
		return "redirect:/";
	}

}

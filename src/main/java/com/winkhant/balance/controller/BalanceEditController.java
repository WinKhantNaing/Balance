package com.winkhant.balance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.winkhant.balance.model.BalanceAppException;
import com.winkhant.balance.model.domain.entity.Balance.Type;
import com.winkhant.balance.model.domain.form.BalanceEditForm;
import com.winkhant.balance.model.domain.form.BalanceItemForm;
import com.winkhant.balance.model.domain.form.BalanceSummaryForm;
import com.winkhant.balance.model.service.BalanceService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("user/balance-edit")
@SessionAttributes("balanceEditForm")
public class BalanceEditController {
	
	@Autowired
	private BalanceService service;

	@GetMapping
	public String edit(@ModelAttribute("balanceEditForm") BalanceEditForm form,
			@RequestParam(required = false) Integer id, 
			@RequestParam(required = false) Type type
			) {	
		
		if(id != null && form.getHeader().getId() != id) {
			var result = service.findById(id);
			form.setHeader(result.getHeader());
			form.setItems(result.getItems());
		}
		
		if(type != null && form.getHeader().getType() != type){
			form.setHeader(new BalanceSummaryForm());
			form.getHeader().setType(type);
			form.getItems().clear();
		}
		
		return "balance-edit";
	}
	
	@PostMapping("item")
	public String addItems(@ModelAttribute("balanceEditForm") BalanceEditForm form,
	@ModelAttribute("balanceItemForm") @Valid BalanceItemForm itemsForm,
	BindingResult result
			) {
		if(result.hasErrors()) {
			return "balance-edit";
		}
		form.getItems().add(itemsForm);
		String query = form.query();
		return "redirect:/user/balance-edit?%s".formatted(query);
	}
	
	@GetMapping("item")
	public String deleteItem(@ModelAttribute("balanceEditForm") BalanceEditForm form, int index) {
		var item = form.getItems().get(index);
		if (item.getId() == 0) {
			form.getItems().remove(item);
		} else {
			item.setDeleted(true);
		}
		String query = form.query();
		
		return "redirect:/user/balance-edit?%s".formatted(query);
	}
	
	
	@GetMapping("confirm")
	public String confirm() {
		return "balance-confirm";
	}
	
	@PostMapping("save")
	public String save(	@ModelAttribute("balanceEditForm") BalanceEditForm form,
			@ModelAttribute("balanceSummaryForm") @Valid BalanceSummaryForm summaryForm,
			BindingResult result
			) {
		if (result.hasErrors()) {
			return "balance-confirm";
		}
		
		form.getHeader().setDate(summaryForm.getDate());
		form.getHeader().setCategory(summaryForm.getCategory());
		int id = service.save(form);
		form.clear();
		return "redirect:/user/balance/details/%d".formatted(id);
	}
	
	@ModelAttribute("balanceEditForm")
	public BalanceEditForm form(@RequestParam(required = false) Integer id, @RequestParam(required = false) Type type) {
		if (id != null) {
			return service.findById(id);
		}
		if (type == null) {
			throw new BalanceAppException("Type must not be null for balance");
		}
		
		return new BalanceEditForm().type(type);
	}
	
	@ModelAttribute("balanceItemForm")
	public BalanceItemForm itemForm() {
		return new BalanceItemForm();
	}
	
	@ModelAttribute("balanceSummaryForm")
	public BalanceSummaryForm summaryForm(@ModelAttribute("balanceEditForm") BalanceEditForm form) {
		return form.getHeader();
	}
}

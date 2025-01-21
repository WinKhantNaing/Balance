package com.winkhant.balance.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.winkhant.balance.controller.utils.Pagination;
import com.winkhant.balance.model.domain.entity.Balance.Type;
import com.winkhant.balance.model.domain.entity.BalanceItem;
import com.winkhant.balance.model.domain.form.BalanceEditForm;
import com.winkhant.balance.model.domain.vo.BalanceReportVo;
import com.winkhant.balance.model.service.BalanceService;

@Controller
@RequestMapping("user/balance")
public class BalanceController {
	
	@Autowired
	private BalanceService service;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@GetMapping
	public String report(
			@RequestParam(required = false) Type type,
			@RequestParam(required = false) LocalDate dateFrom,
			@RequestParam(required = false) LocalDate dateTo,
			Optional<Integer> page,
			Optional<Integer> size,
			Model m
			) {
		Page<BalanceReportVo> result = service.findReport(type,dateFrom,dateTo,page,size);
		
		Pagination pagination = Pagination.builder("/user/balance")
				.page(result)
				.params(Map.of(
						"type", null == type ? "" : type.name(),
						"dateFrom", null == dateFrom ? "" : dateFrom.format(dtf),
						"dateTo", null == dateTo ? "" : dateTo.format(dtf)
						))
				.build(); 	
		m.addAttribute("list", result.getContent());
		m.addAttribute("pagination", pagination);
		return "balance-report";
	}
	
	@GetMapping("{type}")
	public String searchBlaanceItem(
			Model m,
			@PathVariable Type type,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
			@RequestParam(required = false) String keyword,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size) {
		String title = String.format("%s Management".formatted(type));
		Page<BalanceItem> result = service.search(type,dateFrom,dateTo,keyword,page,size);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type.name());
		params.put("dateFrom" , null == dateFrom ? "" : dateFrom.format(dtf));
		params.put("dateTo" , null == dateTo ? "" : dateTo.format(dtf));
		params.put("keyword", keyword == null ? "" : keyword);

		if (result != null) {
			m.addAttribute("list",result.getContent());
			
			Pagination pagination = Pagination.builder("/user/balance/%s".formatted(type))
					.page(result)
					.params(params)
					.build();
		
			m.addAttribute("pagination",pagination);
		} 
		
		m.addAttribute("type", type);
		m.addAttribute("cardtitle", title);
		return "balance-list";
	}
	
	
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable int id) {
		service.deleteById(id);
		return "redirect:/";
	}
	
	@GetMapping("details/{id:\\d+}")
	public String findById(@PathVariable int id,Model m) {
		BalanceEditForm balanceEditForm = service.findById(id);
		m.addAttribute("vo", balanceEditForm);
		return "balance-details";
	}
	
	public String search() {
		return null;
	}
	
	@ModelAttribute("balanceTypes")
	Type[] getType() {
		return Type.values();
	}
}

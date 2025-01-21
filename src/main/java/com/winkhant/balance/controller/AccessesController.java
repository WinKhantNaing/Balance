package com.winkhant.balance.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.winkhant.balance.controller.utils.Pagination;
import com.winkhant.balance.model.domain.entity.AccessLog;
import com.winkhant.balance.model.domain.entity.AccessLog.Type;
import com.winkhant.balance.model.service.AccessLogService;

@Controller
@RequestMapping("admin/accesses")
public class AccessesController {
	
	@Autowired
	private AccessLogService accessLogService;

	@GetMapping
	@PreAuthorize("hasAuthority('Admin')")
	public String search(
			@RequestParam(required = false) Type type,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) LocalDate date,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(required = false) Optional<Integer> size,
			Model m
			) {
		
		Page<AccessLog> list =  accessLogService.searchByAdmin(type, username, date, page, size);
		m.addAttribute("list", list.getContent());
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type == null ? "" : type.name());
		params.put("username", StringUtils.hasLength(username)? username: "");
		params.put("date", date == null ? "" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		Pagination pagination = new Pagination
				.Builder("/admin/accesses")
				.sizeChangeFormId("accessLogSearchForm")
				.page(list).sizes(List.of(5,10,25))
				.params(params).build();
		m.addAttribute("page", pagination);
		return "accesses";
	}
	
	@ModelAttribute("types")
	Type[] types() {
		return Type.values();
	}
}

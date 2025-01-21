package com.winkhant.balance.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winkhant.balance.model.domain.entity.Balance;
import com.winkhant.balance.model.domain.entity.Balance.Type;
import com.winkhant.balance.model.domain.vo.BalanceGraphVo;
import com.winkhant.balance.model.domain.vo.BalanceReportVo;
import com.winkhant.balance.model.service.BalanceService;

@Controller
@RequestMapping("user/home")
public class HomeController {
	
	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired
	BalanceService balanceService;
	
	@GetMapping()
	public String userHome(Model m) {
	        
	        LocalDate currentDate = LocalDate.now();
	        LocalDate startDate = currentDate.withDayOfMonth(1);
	        
	        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	        List<BalanceGraphVo> graphData = balanceService.makeGraph(currentDate, loginId);
	          
	        List<Integer> incomeValues = new ArrayList<>();
	        List<Integer> expenseValues = new ArrayList<>();
	        List<String> labels = new ArrayList<String>();
	        
	        Map<LocalDate, Map<Type, Integer>> grouppedData = balanceService.groupByDateAndType(graphData);
	        
	        for(LocalDate date = startDate; !date.isAfter(currentDate); date = date.plusDays(1)){
	        	labels.add(String.valueOf(date.getDayOfMonth()));
	        	Map<Type, Integer> totals = grouppedData.getOrDefault(date, Map.of());
	        	incomeValues.add(totals.getOrDefault(Type.Income, 0));
	        	expenseValues.add(totals.getOrDefault(Type.Expense, 0));
	        }
	        
	        m.addAttribute("labels", labels);
	        m.addAttribute("incomes", incomeValues);
	        m.addAttribute("expenses", expenseValues);
	       
		return "home";
	}
	


}

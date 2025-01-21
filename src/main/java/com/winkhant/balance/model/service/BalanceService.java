package com.winkhant.balance.model.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.winkhant.balance.model.domain.entity.Balance;
import com.winkhant.balance.model.domain.entity.Balance.Type;
import com.winkhant.balance.model.domain.entity.BalanceItem;
import com.winkhant.balance.model.domain.entity.User;
import com.winkhant.balance.model.domain.form.BalanceEditForm;
import com.winkhant.balance.model.domain.form.BalanceItemForm;
import com.winkhant.balance.model.domain.vo.BalanceGraphVo;
import com.winkhant.balance.model.domain.vo.BalanceReportVo;
import com.winkhant.balance.model.repo.BalanceItemRepo;
import com.winkhant.balance.model.repo.BalanceRepo;
import com.winkhant.balance.model.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class BalanceService {
	
	@Autowired
	private BalanceItemRepo repo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private BalanceRepo balanceRepo;

	@PreAuthorize("authenticated()")
	public Page<BalanceItem> search(Type type, LocalDate dateFrom, LocalDate dateTo, String keyword, Optional<Integer> page,
			Optional<Integer> size) {
		
		Pageable pages = PageRequest.of(page.orElse(0),size.orElse(5));
		
		var userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Specification<BalanceItem> spec = (root, query, builder) -> builder.equal(root.get("balance").get("user").get("loginId"), userName);
		
		spec = spec.and((root, query, builder) -> builder.equal(root.get("balance").get("type"), type));
		
		if (dateFrom != null) {
			spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("balance").get("date"), dateFrom));
		}
		
		if (dateTo != null) {
			spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("balance").get("date"), dateTo));
		}
		
		if (StringUtils.hasLength(keyword)) {
			Specification<BalanceItem> category = (root, query, builder) -> builder.like(builder.lower(root.get("balance").get("category")), 
					"%%%s%%".formatted(keyword).toLowerCase()
					);
			
			Specification<BalanceItem> item = (root, query, builder) -> builder.like(builder.lower(root.get("item")), 
					"%%%s%%".formatted(keyword).toLowerCase()
					);
			
			spec = spec.and(item.or(category));
			
			
		}
		
		
		
		return repo.findAll(spec, pages);
	}

	public BalanceEditForm findById(Integer id) {
		return balanceRepo.findById(id).map(BalanceEditForm::new).orElseThrow();
		
	}

	@Transactional
	public int save(BalanceEditForm form) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findOneByLoginId(username).orElseThrow();
		Balance balance = form.getHeader().getId() == 0 ? new Balance() : balanceRepo.findById(form.getHeader().getId()).orElseThrow();
		balance.setUser(user);
		balance.setCategory(form.getHeader().getCategory());
		balance.setDate(form.getHeader().getDate());
		balance.setType(form.getHeader().getType());
		balance = balanceRepo.save(balance);
		
		for(BalanceItemForm formItem : form.getItems()) {
			var item = formItem.getId() == 0 ? new BalanceItem() : repo.findById(formItem.getId()).orElseThrow();
			if(formItem.isDeleted()) {
				repo.delete(item);
				continue;
			}
			
			item.setBalance(balance);
			item.setItem(formItem.getItem());
			item.setUnitPrice(formItem.getUnitPrice());
			item.setQuantity(formItem.getQuantity());
			
			repo.save(item);
			
		}
		return balance.getId();
	}

	public void deleteById(int id) {
		balanceRepo.deleteById(id);
		
	}

	public Page<BalanceReportVo> findReport(Type type, LocalDate dateFrom, LocalDate dateTo, Optional<Integer> page,
			Optional<Integer> size) {
		
		PageRequest pageInfo = PageRequest.of(page.orElse(0), size.orElse(10));
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Specification<Balance> spec = (root, query, builder) -> builder.equal(root.get("user").get("loginId"), username);
		
		if (null != type) {
			spec = spec.and((root, query, builder) -> builder.equal(root.get("type"), type)); 
		}

		if (null != dateFrom) {
			spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("date"), dateFrom));
		}
		
		if (null != dateTo) {
			spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("date"), dateTo));
		}
		
		Page<BalanceReportVo> result = balanceRepo.findAll(spec, pageInfo).map(BalanceReportVo::new);
		
		if(!result.getContent().isEmpty()) {
			var firstId = result.getContent().get(0).getId();
			var incomes = repo.getLastBalance(username,firstId, Type.Income).map(a -> a.intValue()).orElse(0);
			var expenses = repo.getLastBalance(username,firstId, Type.Expense).map(a -> a.intValue()).orElse(0);
			var lastBalance = incomes - expenses;
			
			for(var vo: result.getContent()) {
				if(vo.getType() == Type.Income) {
					lastBalance += vo.getAmount();					
				} else {
					lastBalance -= vo.getAmount();
				}
				vo.setBalance(lastBalance);
			}
		}
		
		return result;
	}
	
	
	public List<BalanceGraphVo> makeGraph(LocalDate date, String loginId) {
		LocalDate startDate = date.withDayOfMonth(1);
		return balanceRepo.findByDateBetweenAndLoginId(startDate, date, loginId).stream().map(BalanceGraphVo::new).toList(); 
	}
	
	public Map<LocalDate, Map<Type, Integer>> groupByDateAndType(List<BalanceGraphVo> graphData){
		return graphData.stream()
				.collect(Collectors.groupingBy(
							BalanceGraphVo::getDate,
							Collectors.groupingBy(
										BalanceGraphVo::getType,
										Collectors.summingInt(BalanceGraphVo::getAmount)
									)
						));
		
	}
	
}

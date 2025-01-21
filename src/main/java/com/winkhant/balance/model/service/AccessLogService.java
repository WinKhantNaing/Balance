package com.winkhant.balance.model.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.winkhant.balance.model.domain.entity.AccessLog;
import com.winkhant.balance.model.domain.entity.AccessLog.Type;
import com.winkhant.balance.model.repo.AccessLogRepo;

@Service
public class AccessLogService {
	
	@Autowired
	private AccessLogRepo accessLogRepo;

	public Page<AccessLog> search(String loginId, Optional<Integer> page, Optional<Integer> size) {
		
		PageRequest pageInfo = PageRequest.of(page.orElse(0), size.orElse(5)).withSort(Sort.by("accessTime").descending());
		Specification<AccessLog> spec = (root,query,builder) -> builder.equal(root.get("userName"), loginId);
		return accessLogRepo.findAll(spec, pageInfo);
	}

	public Page<AccessLog> searchByAdmin(Type type, String username, LocalDate date, Optional<Integer> page,
			Optional<Integer> size) {
		
		Pageable pageInfo = PageRequest.of(page.orElse(0), size.orElse(5)).withSort(Sort.by("accessTime").descending());
		
		Specification<AccessLog> spec = Specification.where(null);
		
		if(type != null) {
			spec = spec.and((root, query, builder) -> builder.equal(root.get("type"), type) );
		}
		
		if(StringUtils.hasLength(username)) {
			spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("userName")), username.toLowerCase().concat("%")));
		}
		
		if(date != null) {
			spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("accessTime"), date));
		}
		
		return accessLogRepo.findAll(spec, pageInfo);
		
	}
	
}

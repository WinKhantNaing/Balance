package com.winkhant.balance.model.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.winkhant.balance.model.domain.entity.Balance;

@Repository
public interface BalanceRepo extends JpaRepository<Balance, Integer>, JpaSpecificationExecutor<Balance> {
	
	@Query("select b from Balance b where b.date between :startDate and :endDate and b.user.loginId = :loginId")
	public List<Balance> findByDateBetweenAndLoginId(LocalDate startDate, LocalDate endDate, String loginId);
	
}

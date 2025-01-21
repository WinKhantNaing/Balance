package com.winkhant.balance.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.winkhant.balance.model.domain.entity.Balance.Type;
import com.winkhant.balance.model.domain.entity.BalanceItem;

@Repository
public interface BalanceItemRepo extends JpaRepository<BalanceItem, Integer>, JpaSpecificationExecutor<BalanceItem>{

	@Query("select sum (bi.unitPrice * bi.quantity) from BalanceItem bi where bi.balance.user.loginId = :loginId and bi.balance.id < :id and bi.balance.type = :type")
	Optional<Number> getLastBalance(String loginId,int id, Type type);

	

}

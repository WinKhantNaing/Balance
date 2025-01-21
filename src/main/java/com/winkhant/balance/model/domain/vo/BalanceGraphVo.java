package com.winkhant.balance.model.domain.vo;

import java.time.LocalDate;

import com.winkhant.balance.model.domain.entity.Balance;
import com.winkhant.balance.model.domain.entity.Balance.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceGraphVo {
	
	public BalanceGraphVo() {
	}
	
	public BalanceGraphVo(Balance entity) {
		this.type = entity.getType();
		this.date = entity.getDate();
		this.amount = entity.getItems().stream().mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
	}
	
	private Type type;
	private int amount;
	LocalDate date;	                                                                                                                                                                                                                                                                                                           
}

package com.winkhant.balance.model.domain.vo;

import java.time.LocalDate;

import com.winkhant.balance.model.domain.entity.Balance;
import com.winkhant.balance.model.domain.entity.Balance.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceReportVo {
	
	private int id;
	private LocalDate date;
	private Type type;
	private String category;
	private int amount;
	private int balance;
	
	public BalanceReportVo() {
	}
	
	public BalanceReportVo(Balance entity) {
		this.id = entity.getId();
		this.date = entity.getDate();
		this.type = entity.getType();
		this.category = entity.getCategory();
		this.amount = entity.getItems().stream().mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
	}
	
	public int getIncome() {
		return type == Type.Income ? amount : 0;
	}
	
	public int getExpense() {
		return type == Type.Expense ? amount : 0;
	}
	
}

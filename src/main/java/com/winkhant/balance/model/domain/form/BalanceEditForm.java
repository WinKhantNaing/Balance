package com.winkhant.balance.model.domain.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.winkhant.balance.model.domain.entity.Balance;
import com.winkhant.balance.model.domain.entity.Balance.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceEditForm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	public BalanceEditForm() {
		header = new BalanceSummaryForm();
		items = new ArrayList<>();
	}
	
	public BalanceEditForm(Balance entity) {
		header = new BalanceSummaryForm();
		header.setId(entity.getId());
		header.setDate(entity.getDate());
		header.setCategory(entity.getCategory());
		header.setType(entity.getType());
		
		items = new ArrayList<>(
				entity.getItems().stream().map(a -> {
					var item = new BalanceItemForm();
					item.setId(a.getId());
					item.setItem(a.getItem());
					item.setQuantity(a.getQuantity());
					item.setUnitPrice(a.getUnitPrice());
					return item;
				}).toList()
				);
	}
	
	public BalanceEditForm type(Type type) {
		header.setType(type);
		return this;
	}
	
	private BalanceSummaryForm header;
	private List<BalanceItemForm> items;
	
	public int getTotal() {
		int total = items.stream().filter(a -> !a.isDeleted()).mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
		return total;
	}
	
	public int getTotalCount() {
		int totalCount = items.stream().filter(a -> !a.isDeleted()).mapToInt(a -> a.getQuantity()).sum();
		return totalCount;
	}
	
	public boolean isShowSaveBtn() {
		return !items.isEmpty();
	}

	public String query() {
		return header.getId() == 0 ? "type=%s".formatted(header.getType()) : "id=%s".formatted(header.getId());
	}
	
	public List<BalanceItemForm> getValidItems(){
		return items.stream().filter(a -> !a.isDeleted()).toList();
	}

	public void clear() {
		items.clear();
		header = new BalanceSummaryForm();
	}
}

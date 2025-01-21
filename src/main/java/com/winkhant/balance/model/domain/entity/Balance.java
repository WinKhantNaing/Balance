package com.winkhant.balance.model.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Balance implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private LocalDate date;
	@Column(nullable=false)
	private String category;
	@ManyToOne(optional=false,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private User user;
	private Type type;
	
	private Balance balance;
	
	@OneToMany(mappedBy = "balance", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<BalanceItem> items;
	
	public Balance() {
		items = new ArrayList<>();
	}
	
	public void addItem(BalanceItem item) {
		 item.setBalance(this);
		 items.add(item);
	}
	
	public enum Type{
		Income,
		Expense
	}

}

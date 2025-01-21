package com.winkhant.balance.model.domain.form;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceItemForm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	@NotBlank(message = "Item name must not be null.")
	private String item;
	@Min(value=1, message = "Enter price.")
	private int unitPrice;
	@Min(value=1, message = "Enter quantity")
	private int quantity;
	private boolean deleted;

}

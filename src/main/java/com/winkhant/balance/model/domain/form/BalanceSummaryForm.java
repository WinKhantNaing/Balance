package com.winkhant.balance.model.domain.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.winkhant.balance.model.domain.entity.Balance.Type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceSummaryForm {
	
	private int id;
	@NotNull(message = "Enter Date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	@NotBlank(message = "Enter Category")
	private String category;
	private Type type;
}

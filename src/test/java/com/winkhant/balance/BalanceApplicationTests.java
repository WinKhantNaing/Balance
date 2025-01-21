package com.winkhant.balance;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.winkhant.balance.model.domain.entity.Balance;
import com.winkhant.balance.model.repo.BalanceRepo;

@SpringBootTest
class BalanceApplicationTests {

	@Autowired
	private BalanceRepo repo;
	
	@Test
	void contextLoads() {
		LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 13);
        String loginId = "spring";
        
        List<Balance> balances = repo.findByDateBetweenAndLoginId(startDate, endDate, loginId);

        assertNotNull(balances);
        balances.forEach(System.out::println);
	}

}

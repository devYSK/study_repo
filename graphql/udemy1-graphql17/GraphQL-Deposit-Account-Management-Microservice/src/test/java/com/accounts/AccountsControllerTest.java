package com.accounts;

import com.accounts.controller.AccountsController;
import com.accounts.domain.AccountStatus;
import com.accounts.domain.AccountType;
import com.accounts.domain.InterestRateType;
import com.accounts.entity.DepositAccount;
import com.accounts.service.DepositService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(AccountsController.class)
public class AccountsControllerTest {

    @Autowired
    private AccountsController accountsController;

    @MockBean
    private DepositService depositService;

    @Test
    public void testAccounts() {
        List<DepositAccount> mockAccounts = Arrays.asList(
                new DepositAccount(
                        "1",
                        AccountType.LOAN, // Assuming AccountType is an enum or similar
                        "123456789",
                        "1234-567-89",
                        "Emergency Fund",
                        AccountStatus.OPEN, // Assuming AccountStatus is an enum or similar
                        "High-yield savings account for emergencies.",
                        "Retail Banking",
                        "111000025",
                        1.5f,
                        InterestRateType.FIXED, // Assuming InterestRateType is an enum or similar
                        LocalDateTime.now().minusDays(10),
                        "USD",
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now(),
                        5000f,
                        5000f,
                        5000f,
                        1.5f,
                        50f,
                        null,
                        null
                ),
                new DepositAccount(
                        "2",
                        AccountType.CHECKING,
                        "987654321",
                        "9876-543-21",
                        "Daily Spending",
                        AccountStatus.DELINQUENT,
                        "Checking account for daily expenses.",
                        "Consumer Banking",
                        "111000026",
                        0.1f,
                        InterestRateType.VARIABLE,
                        LocalDateTime.now().minusDays(15),
                        "USD",
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        2000f,
                        2000f,
                        1800f,
                        0.1f,
                        5f,
                        null,
                        null
                ),
                new DepositAccount(
                        "3",
                        AccountType.CD,
                        "112233445",
                        "1122-334-45",
                        "Long Term Savings",
                        AccountStatus.CLOSED,
                        "Certificate of Deposit, locked for 12 months.",
                        "Investment Banking",
                        "111000027",
                        2.5f,
                        InterestRateType.FIXED,
                        LocalDateTime.now().minusDays(20),
                        "USD",
                        LocalDateTime.now().minusDays(3),
                        LocalDateTime.now().minusDays(2),
                        10000f,
                        10000f,
                        10000f,
                        2.5f,
                        200f,
                        12,
                        LocalDateTime.now().plusMonths(12)
                )
                // Add more accounts as needed
        );

        when(depositService.getAccounts()).thenReturn(mockAccounts);

        List<DepositAccount> accounts = accountsController.getAllAccounts();

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(mockAccounts.size(), accounts.size());
        verify(depositService, times(1)).getAccounts();
    }
}
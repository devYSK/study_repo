package com.accounts;

import com.accounts.entity.DepositAccount;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = SpringBootGraphQLApplication.class)


public class AccountsGraphQlTest {
    @Autowired
    private GraphQlTester graphQlTester;
    @Test
    public void testAddAccountsMutation() {
        Boolean addAccountResult =  graphQlTester.document("mutation {\n" +
                        "    addAccount(account: {\n" +
                        "        accountId: \"A100\",\n" +
                        "        accountType: CHECKING,\n" +
                        "        accountNumber: \"123456789\",\n" +
                        "        accountNumberDisplay: \"1234-567-89\",\n" +
                        "        nickName: \"Emergency Fund\",\n" +
                        "        status: OPEN,\n" +
                        "        description: \"High-yield savings account for emergency funds.\",\n" +
                        "        lineOfBusiness: \"Retail Banking\",\n" +
                        "        routingTransitNumber: \"111000025\",\n" +
                        "        interestRate: 1.5,\n" +
                        "        interestRateType: FIXED,\n" +
                        "        interestRateAsOf: \"2024-01-19T16:39:57-08:00\",\n" +
                        "        currency: \"USD\",\n" +
                        "        lastActivityDate: \"2024-01-19T16:39:57-08:00\",\n" +
                        "        balanceAsOf: \"2024-01-19T16:39:57-08:00\",\n" +
                        "        currentBalance: 3445,\n" +
                        "        openingDayBalance: 4444,\n" +
                        "        availableBalance: 79872,\n" +
                        "        annualPercentageYield: 1.54,\n" +
                        "        interestYtd: 5,\n" +
                        "        term: null,\n" +
                        "        maturityDate: null\n" +
                        "    })\n" +
                        "}").execute()
                .errors()
                .verify()
                .path("addAccount")
                .entity(Boolean.class)
                .get();

        assertThat(addAccountResult).isNotNull();
        assertThat(addAccountResult).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void testAccountsQuery() {
        java.util.List<DepositAccount> bankAccounts =  graphQlTester.document("query{\n" +
                        "    getAllAccounts {\n" +
                        "      accountId\n" +
                        "      accountType\n" +
                        "      accountNumber\n" +
                        "      accountNumberDisplay\n" +
                        "      nickName\n" +
                        "      status\n" +
                        "      description\n" +
                        "      lineOfBusiness\n" +
                        "      routingTransitNumber\n" +
                        "      interestRate\n" +
                        "      interestRateType\n" +
                        "      interestRateAsOf\n" +
                        "      currency\n" +
                        "      lastActivityDate\n" +
                        "      balanceAsOf\n" +
                        "      currentBalance\n" +
                        "      openingDayBalance\n" +
                        "      availableBalance\n" +
                        "      annualPercentageYield\n" +
                        "      interestYtd\n" +
                        "      term\n" +
                        "      maturityDate\n" +
                        "  }\n" +
                        "}").execute()
                .errors()
                .verify()
                .path("getAllAccounts")
                .entity(ArrayList.class)
                .get();

        assertThat(bankAccounts).isNotNull();
        assertThat(bankAccounts.size()).isEqualTo(1);
    }
}
package com.fastcampuspay.money.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMoney {
	private final String memberMoneyId;

	private final String membershipId;

	// 잔액
	private final int balance;

	//  private final int linkedBankAccount;

	public static MemberMoney generateMemberMoney(
		MemberMoneyId memberMoneyId,
		MembershipId membershipId,
		MoneyBalance moneyBalance
	) {
		return new MemberMoney(
			memberMoneyId.memberMoneyId,
			membershipId.membershipId,
			moneyBalance.balance
		);
	}

	@Value
	public static class MemberMoneyId {
		public MemberMoneyId(String value) {
			this.memberMoneyId = value;
		}

		String memberMoneyId;
	}

	@Value
	public static class MembershipId {
		public MembershipId(String value) {
			this.membershipId = value;
		}

		String membershipId;
	}

	@Value
	public static class MoneyBalance {
		public MoneyBalance(int value) {
			this.balance = value;
		}

		int balance;
	}

	public record MoneyAggregateIdentifier(String aggregateIdentifier) {
	}
}

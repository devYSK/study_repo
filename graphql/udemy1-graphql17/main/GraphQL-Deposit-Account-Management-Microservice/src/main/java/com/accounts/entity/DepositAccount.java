package com.accounts.entity;

import com.accounts.domain.AccountStatus;
import com.accounts.domain.AccountType;
import com.accounts.domain.DateTimeConverter;
import com.accounts.domain.InterestRateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "DepositAccount", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountId"})
})
public class DepositAccount {
    @Id
    @Column
    private  String accountId;

    @Column
    private AccountType accountType;

    @Column
    private  String  accountNumber;

    @Column
    private  String  accountNumberDisplay;

    @Column
    private  String  nickName;

    @Column
    private AccountStatus status;

    @Column
    private  String  description;

    @Column
    private  String  lineOfBusiness;

    @Column
    private  String  routingTransitNumber;

    @Column
    private  Float  interestRate;

    @Column
    private InterestRateType interestRateType;

    @Column
    private LocalDateTime interestRateAsOf;

    @Column
    private  String  currency;

    @Column
    private  LocalDateTime  lastActivityDate;

    @Column
    private  LocalDateTime  balanceAsOf;

    @Column
    private  Float  currentBalance;

    @Column
    private  Float  openingDayBalance;

    @Column
    private  Float  availableBalance;

    @Column
    private  Float  annualPercentageYield;

    @Column
    private  Float  interestYtd;

    @Column
    private  Integer  term;

    @Column
    private  LocalDateTime  maturityDate;

    public OffsetDateTime getMaturityDate() {
        return DateTimeConverter.toOffsetDateTime(this.maturityDate);
    }

    public OffsetDateTime getBalanceAsOf() {
        return DateTimeConverter.toOffsetDateTime(this.balanceAsOf);
    }

    public OffsetDateTime getLastActivityDate() {
        return DateTimeConverter.toOffsetDateTime(this.lastActivityDate);
    }

    public OffsetDateTime getInterestRateAsOf() {
        return DateTimeConverter.toOffsetDateTime(this.interestRateAsOf);
    }
}

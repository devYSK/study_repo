package com.accounts.entity;

import com.accounts.domain.Client;
import com.accounts.domain.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "BankAccounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column
    private Long clientId;

    @Column
    private Currency currency;

    @Column
    private String country;

    @Column
    private Float balance;

    @Column
    private String status;

    @Column
    private Float transferLimit;

    @Column
    private LocalDateTime accountCreateDate;
}

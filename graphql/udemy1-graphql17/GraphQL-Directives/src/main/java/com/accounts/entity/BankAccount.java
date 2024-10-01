package com.accounts.entity;

import com.accounts.domain.Client;
import com.accounts.domain.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private Float balance;

    @Column
    private String status;
}

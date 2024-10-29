package com.yscorp.ex1.entity

import com.yscorp.ex1.domain.Currency
import jakarta.persistence.*

@Entity
@Table(name = "BankAccounts")
class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @Column
    var clientId: Long? = null

    @Column
    var currency: Currency? = null

    @Column
    var balance: Float? = null

    @Column
    var status: String? = null

}

package com.yscorp.ex1.repository

import com.yscorp.ex1.entity.BankAccount
import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepo : JpaRepository<BankAccount, Long>

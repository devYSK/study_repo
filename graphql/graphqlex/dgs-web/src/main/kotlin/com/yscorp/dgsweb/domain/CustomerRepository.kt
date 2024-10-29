package com.yscorp.dgsweb.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*


interface CustomerRepository : JpaRepository<Customer, UUID>,
    JpaSpecificationExecutor<Customer>

package com.yscorp.reactiver2dbc.domain

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : CoroutineCrudRepository<Customer, Int> {

}


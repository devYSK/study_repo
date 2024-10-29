package com.yscorp.dgsweb.domain

import jakarta.persistence.*
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "customers")
data class Customer(
    @Id
    @GeneratedValue
    var uuid: UUID,

    @Column(unique = true)
    var email: String,

    var birthDate: LocalDate,

    var fullName: String,

    var phone: String,

    @OneToMany
    @JoinColumn(name = "customer_uuid")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    var addresses: MutableList<Address> = mutableListOf(),

    @OneToMany
    @JoinColumn(name = "customer_uuid")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    var documents: MutableList<CustomerDocument> = mutableListOf(),
)

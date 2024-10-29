package com.yscorp.dgsweb.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "customer_documents")
data class CustomerDocument(
    @Id
    @GeneratedValue
    var uuid: UUID? = null,

    var documentType: String? = null,

    var documentPath: String? = null

)

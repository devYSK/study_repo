package com.yscorp.reactivemongo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "students")
class Student(
    @Id
    val id: String,
    val name: String,
    val marks: Double = 0.0,

    ) {
}

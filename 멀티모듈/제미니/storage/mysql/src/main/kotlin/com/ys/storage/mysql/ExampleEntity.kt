package com.ys.storage.mysql

import com.ys.storage.mysql.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class ExampleEntity(
    @Column
    val exampleColumn: String,
) : BaseEntity()

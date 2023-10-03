package com.ys.storage.mysql

import jakarta.persistence.Entity

@Entity
internal class UserEntity(
    val name: String

) : BaseEntity() {

}

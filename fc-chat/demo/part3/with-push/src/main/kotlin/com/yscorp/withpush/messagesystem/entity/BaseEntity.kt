package com.yscorp.withpush.messagesystem.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @Column(name = "created_at", updatable = false, nullable = false)
    var createAt: LocalDateTime? = null
        private set

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
        private set

    @PrePersist
    fun prePersist() {
        this.createAt = LocalDateTime.now()
        this.updatedAt = this.createAt
    }

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}

package com.yscorp.web1.domain

import jakarta.persistence.Embeddable
import java.io.Serializable


class ResultID(
    val student: Long = 0,  // Member의 ID
    val subject: Long = 0   // Subject의 ID
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as ResultID

        if (student != other.student) return false
        if (subject != other.subject) return false

        return true
    }

    override fun hashCode(): Int {
        var result = student.hashCode()
        result = 31 * result + subject.hashCode()
        return result
    }
}
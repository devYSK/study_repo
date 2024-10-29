package com.yscorp.web1.domain.dto

data class StudentResponse(
    val id: Long,

    val name: String,

    val contact: String,

    val result: List<StudentSubjectResponse>,

    ) {
}

package com.yscorp.web1.domain.dto

import javax.naming.directory.SearchResult

class StudentSubjectResponse(
    val marks: Double = 0.0,
    override val subjectName: String
) : SubjectResponse(subjectName) {

}

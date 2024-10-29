package com.yscorp.web1.domain

import com.yscorp.web1.domain.dto.MemberResponse
import com.yscorp.web1.domain.dto.MemberSearchResult
import com.yscorp.web1.domain.dto.StudentSubjectResponse
import org.springframework.stereotype.Service


@Service
class ResultService(
    val repository: ResultRepository,
) {

    fun getResultsForStudents(responses: List<MemberResponse>): Map<MemberResponse, List<StudentSubjectResponse>> {
        val results = repository.findAll()

        return responses.associateWith { response ->
            results.filter { it.student.id == response.id }
                .map { StudentSubjectResponse(it.marks, it.subject.subjectName) }
        }
    }

    fun getResultsForSearch(responses: List<MemberSearchResult>): Map<MemberSearchResult, List<StudentSubjectResponse>> {
        val results = repository.findAll()

        return responses.associateWith { response ->
            results.filter { it.student.id == response.id }
                .map { StudentSubjectResponse(it.marks, it.subject.subjectName) }
        }
    }

    fun getResultsForStudent(id: Long): List<StudentSubjectResponse> {
        val results = repository.findAll()

        return results.filter { it.student.id == id }
            .map { StudentSubjectResponse(it.marks, it.subject.subjectName) }
    }

}
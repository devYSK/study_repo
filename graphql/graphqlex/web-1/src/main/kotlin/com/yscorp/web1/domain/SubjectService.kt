package com.yscorp.web1.domain

import com.yscorp.web1.domain.dto.MemberResponse
import com.yscorp.web1.domain.dto.MemberSearchResult
import com.yscorp.web1.domain.dto.TeacherSubjectResponse
import org.springframework.stereotype.Service


@Service
class SubjectService(
    val repository: SubjectRepository,
) {

    fun getSubjectsInfoForTeachers(responses: List<MemberResponse>): Map<MemberResponse, List<TeacherSubjectResponse>> {
        val subjects = repository!!.findAll()

        return responses.associateWith { response ->
            subjects.filter { it.teacher.id == response.id }
                .map { TeacherSubjectResponse(it.experience, it.subjectName) }
        }
    }

    fun getSubjectSearchResults(responses: List<MemberSearchResult>): Map<MemberSearchResult, List<TeacherSubjectResponse>> {
        val subjects = repository!!.findAll()

        return responses.associateWith { response ->
            subjects.filter { it.teacher.id == response.id }
                .map { TeacherSubjectResponse(it.experience, it.subjectName) }
        }
    }

}
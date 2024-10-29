package com.yscorp.web1.domain.dto

class TeacherSubjectResponse(
   val experience: Int,
   override val subjectName: String,
) : SubjectResponse(subjectName){

}

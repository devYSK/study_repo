package com.yscorp.web1.interfaces

import com.yscorp.web1.domain.MemberService
import com.yscorp.web1.domain.MemberType
import com.yscorp.web1.domain.ResultService
import com.yscorp.web1.domain.SubjectService
import com.yscorp.web1.domain.dto.MemberResponse
import com.yscorp.web1.domain.dto.MemberSearchResult
import com.yscorp.web1.domain.dto.StudentResponse
import com.yscorp.web1.domain.dto.StudentSubjectResponse
import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.function.Consumer

@Controller
class Query(
    private val memberService: MemberService,
    private val resultService: ResultService,
    private val subjectService: SubjectService,
) {

    @QueryMapping
    fun firstQuery(): String {
        throw ArithmeticException("Arithmetic exception occurred1111111")

        return "Hello, GraphQL!"
    }

    @QueryMapping
    fun secondQuery(@Argument firstName: String, @Argument lastName: String): String {
        return "$firstName $lastName"
    }

    @QueryMapping
    fun thirdQuery() : String {
        throw ArithmeticException("Arithmetic exception occurred1111111")
    }

    @QueryMapping
    fun queryWithInterceptor(
        dfe: DataFetchingEnvironment,
        @ContextValue testHeader: String,
        context: GraphQLContext,
    ): String {
        //String value = dfe.getGraphQlContext().get("testHeader");
        context.put("setHeader1", "test1")
        dfe.graphQlContext.put("setHeader2", "test2")
        return "Hello $testHeader"
    }


    @QueryMapping(name = "getMembers")
    fun getMembers(@Argument("filter") type: MemberType?): List<MemberResponse> {
        // fetch all students records
        println(":: fetching all members ::")
        return memberService.getMembers(type)
    }

    @BatchMapping(typeName = "MemberResponse", field = "subjectData")
    fun getSubjectsData(members: List<MemberResponse>): Map<MemberResponse, List<*>> {
        println(":: fetching all members subject data ::")

        val students = mutableListOf<MemberResponse>()
        val teachers = mutableListOf<MemberResponse>()

        members.forEach { member ->
            when (member.type) {
                MemberType.TEACHER -> teachers.add(member)
                else -> students.add(member)
            }
        }

        val outputMap = mutableMapOf<MemberResponse, List<*>>().apply {
            if (teachers.isNotEmpty()) {
                println(":: fetching teachers subject data ::")
                putAll(subjectService.getSubjectsInfoForTeachers(teachers))
            }
            if (students.isNotEmpty()) {
                println(":: fetching students subject data ::")
                putAll(resultService.getResultsForStudents(students))
            }
        }

        return outputMap
    }

    @QueryMapping("searchByName")
    fun getSearchResult(@Argument text: String): List<MemberSearchResult> {
        return memberService.getSearchResult(text)
    }

    @SchemaMapping(typeName = "StudentResponse", field = "result")
    fun getStudentResult(student: StudentResponse): List<StudentSubjectResponse> {
        return resultService.getResultsForStudent(student.id)
    }

}
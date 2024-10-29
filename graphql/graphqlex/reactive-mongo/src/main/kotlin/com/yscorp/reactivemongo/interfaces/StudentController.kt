package com.yscorp.reactivemongo.interfaces

import com.fasterxml.jackson.databind.ObjectMapper
import com.yscorp.reactivemongo.domain.Student
import com.yscorp.reactivemongo.domain.StudentInput
import com.yscorp.reactivemongo.domain.StudentService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many


@Controller
class StudentController(
    @Autowired private val service: StudentService,
    @Autowired private val mapper: ObjectMapper,
    @Autowired private val studentSink: Sinks.Many<Student>,
) {

    @Bean
    fun studentSinkConfig(): Many<Student> {
        return Sinks.many().multicast().directBestEffort()
    }

    @QueryMapping("getAllStudents")
    fun getAll(): Flow<Student> {
        return service.getAllStudents()
    }

    @MutationMapping("addStudent")
    suspend fun addStudent(@Argument("input") input: StudentInput): Student {
        val student = mapper.convertValue(input, Student::class.java)
        val addedStudent = service.addStudent(student) // Mono를 suspend로 처리
        studentSink.tryEmitNext(addedStudent) // studentSink에 추가

        return addedStudent
    }

    @SubscriptionMapping
    fun studentSubscription(): Flow<Student> {
        println("Student Subscription done")
        println("Current count :: ${studentSink.currentSubscriberCount()}")
        return studentSink.asFlux().asFlow() // Flux를 Flow로 변환하여 리턴
    }

}
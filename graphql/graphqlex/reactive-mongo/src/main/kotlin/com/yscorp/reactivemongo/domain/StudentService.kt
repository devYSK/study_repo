package com.yscorp.reactivemongo.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val repository: StudentRepository,
) {

    fun getAllStudents(): Flow<Student> {
        return repository.findAll().asFlow()
    }

    suspend fun addStudent(student: Student): Student {
        return repository.insert(student).awaitSingle()
    }

}

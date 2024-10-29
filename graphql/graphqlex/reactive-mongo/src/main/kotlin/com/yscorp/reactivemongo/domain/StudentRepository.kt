package com.yscorp.reactivemongo.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : ReactiveMongoRepository<Student, String>

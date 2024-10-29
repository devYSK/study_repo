package com.yscorp.web1.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Int>

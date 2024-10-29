package com.yscorp.web1.domain

import jakarta.persistence.*


@Entity
@Table(name = "result")
@IdClass(ResultID::class)
class Result(
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    val student: Member,

    @Id
    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    val subject: Subject,

    @Column(name = "marks")
    var marks: Double = 0.0
)

package com.yscorp.web1.domain

import jakarta.persistence.*

@Entity
@Table(name = "subject")
class Subject(

    @Column(name = "subject_name")
    var subjectName: String,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    val teacher: Member,
) {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0

    @Column(name = "experience")
    var experience = 0
}

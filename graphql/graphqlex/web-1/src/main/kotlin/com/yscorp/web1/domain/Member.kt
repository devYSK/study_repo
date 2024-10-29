package com.yscorp.web1.domain

import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: MemberType = MemberType.STUDENT

    @Column(name = "contact")
    var contact: String? = null

}

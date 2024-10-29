package com.yscorp.web1.domain.dto

import com.yscorp.web1.domain.Member
import com.yscorp.web1.domain.MemberType

data class MemberResponse(
    val id: Long = 0,
    val name: String? = null,
    val contact: String? = null,
    val type: MemberType? = null,
) {

    constructor(member: Member) : this(
        id = member.id,
        name = "${member.firstName} ${member.lastName}",
        contact = member.contact,
        type = member.type
    )
}

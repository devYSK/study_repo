package com.yscorp.web1.domain.dto

import com.yscorp.web1.domain.Member
import com.yscorp.web1.domain.MemberType

data class MemberSearchResult(
    val id: Long,

    val name: String?,
    val contact: String?,
    val type: MemberType?,

    ) {

    constructor(member: Member) : this(
        id = member.id,
        name = "${member.firstName} ${member.lastName}",
        contact = member.contact,
        type = member.type
    )
}

package com.yscorp.web1.domain.dto

import com.yscorp.web1.domain.MemberType

class MemberInput(
    val firstName: String,
    val lastName: String? = null,
    val type: MemberType,
    val contact: String,
) {
}

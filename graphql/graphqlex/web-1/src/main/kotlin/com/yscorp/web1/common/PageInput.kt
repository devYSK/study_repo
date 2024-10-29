package com.yscorp.web1.common

data class PageInput(
    val offset: Int,
    val limit: Int,
    val after: String? = null,
    val before: String? = null,

    ) {
}

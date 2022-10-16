package com.group.libraryapp.dto.book.response

import com.group.libraryapp.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    var count: Long,
) {

    fun plusOne() {
        this.count += 1
    }
}
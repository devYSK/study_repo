package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import javax.persistence.*

@Entity
class User constructor(
    name: String,
    var age: Int?,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val userLoanHistories: MutableList<UserLoanHistory> =
        mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    ) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다");
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name, UserLoanStatus.LOANED))
    }

    fun returnBook(bookName: String) {
        this.userLoanHistories.first { history -> history.bookName == bookName }
            .doReturn()
    }

    var name = name
        private set
}
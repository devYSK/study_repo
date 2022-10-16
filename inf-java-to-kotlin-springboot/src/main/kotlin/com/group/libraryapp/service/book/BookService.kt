package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.repository.BookQuerydslRepository
import com.group.libraryapp.repository.UserLoanHistoryQuerydslRepository
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryQuerydslRepository,
    private val bookQuerydslRepository: BookQuerydslRepository,
) {


    @Transactional
    fun saveBook(request: BookRequest) {
        bookRepository.save(Book(request.name, request.type))
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        if (userLoanHistoryRepository.find(
                request.bookName,
                UserLoanStatus.LOANED
            ) != null
        ) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        val book = bookRepository.findByName(request.bookName) ?: fail()
        val user = userRepository.findByName(request.userName) ?: fail()

        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException()
        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
//        return userLoanHistoryRepository.findAllByStatus(UserLoanStatus.LOANED).size

        return userLoanHistoryRepository.count(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        return bookQuerydslRepository.getStats()
    }

}
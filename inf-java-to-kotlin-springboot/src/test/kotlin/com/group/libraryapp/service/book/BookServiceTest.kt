package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val bookService: BookService

) {

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록이 정상 동작한다")
    fun saveBookTest() {
        // given
        val request = BookRequest(Book.ELIS, BookType.SOCIETY)
        // when
        bookService.saveBook(request)
        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(
            books[0]
                .name
        ).isEqualTo("이상한 나라의 엘리스")
        assertThat(books[0].type).isEqualTo(BookType.SOCIETY)
    }

    @Test
    @DisplayName("책 대출이 정상 동작한다")
    fun loanBookTest() {
        // given
        bookRepository.save(Book.fixture(Book.ELIS, BookType.SOCIETY))

        val savedUser = userRepository.save(User("김영수", null))
        val request = BookLoanRequest("김영수", Book.ELIS)
        // when
        bookService.loanBook(request)
        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo(Book.ELIS)
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    @DisplayName("책이 진작 대출되어 있다면, 신규 대출이 실패한다")
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book.fixture(Book.ELIS, BookType.SOCIETY))
        val savedUser = userRepository.save(User("김영수", null))

        userLoanHistoryRepository.save(
            UserLoanHistory(
                savedUser,
                "이상한 나라의 앨리스",
                UserLoanStatus.LOANED
            )
        )

        val request = BookLoanRequest("김영수", "이상한 나라의 앨리스")

        //when & then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }

    }

    @Test
    @DisplayName("책 반납이 정상 동작한다")
    fun returnBookTest() {
        // given
        bookRepository.save(Book.fixture("이상한 나라의 앨리스", BookType.SOCIETY))
        val savedUser = userRepository.save(User("최태현", null))
        userLoanHistoryRepository.save(
            UserLoanHistory.fixture(
                savedUser
            )
        )
        val request = BookReturnRequest("최태현", "이상한 나라의 엘리스")
        // when
        bookService.returnBook(request)
        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @DisplayName("책 대여 권수를 정상 확인한다")
    @Test
    fun countLoanedBookTest() {
        //given
        val savedUser = userRepository.save(User("김영수", null))

        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "A"),
                UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(savedUser, "C", UserLoanStatus.RETURNED)
            )
        )
        //when
        val result = bookService.countLoanedBook()
        //then
        assertThat(result).isEqualTo(1)
    }

    @DisplayName("분야별 책 권수를 정상 확인한다.")
    @Test
    fun getBookStatisticsTest() {
        //given
        bookRepository.saveAll(
            listOf(
                Book.fixture("A", BookType.COMPUTER),
                Book.fixture("B", BookType.COMPUTER),
                Book.fixture("C", BookType.SCIENCE),
                ))
        //when
        val result = bookService.getBookStatistics()
        //then
        assertThat(result).hasSize(2)
        assertCount(result, BookType.COMPUTER, 2)
        assertCount(result, BookType.SCIENCE, 1)
    }

    private fun assertCount(result: List<BookStatResponse>, type: BookType, expectedCount: Long) {
        assertThat(result.first {it.type == type}.count).isEqualTo(expectedCount)
    }
}
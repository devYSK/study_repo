package com.yscorp.dgsweb

import net.datafaker.Faker
import org.junit.jupiter.api.Test
import java.util.*

class FakerTest {

    val faker = Faker()

    @Test
    fun generateKorean() {

        val faker1 = Faker(Locale.of("ko"))


        repeat(100) {

            // 한국어 이름 생성
            val name = faker.name().fullName()

            // 한국어 주소 생성
            val address = faker.address().fullAddress()

            // 한국어 전화번호 생성
            val phoneNumber = faker.phoneNumber().cellPhone()

            // 생년월일 생성 (20세에서 50세 사이)
            val dateOfBirth = faker.date().birthday(20, 50).toString()

            // 이메일 생성
            val email = faker.internet().emailAddress()

            // 결과 출력
            println("이름: $name")
            println("주소: $address")
            println("전화번호: $phoneNumber")
            println("생년월일: $dateOfBirth")
            println("이메일: $email")
            println(faker1.lorem().paragraphs(5))

            // 회사명 생성
            val companyName = faker.company().name()

            // 직함 생성
            val jobTitle = faker.job().title()

            // 슬로건 생성
            val catchPhrase = faker.company().catchPhrase()

            // 회사 비전 생성
            val bs = faker.company().bs()

            // 결과 출력
            println("회사명: $companyName")
            println("직함: $jobTitle")
            println("슬로건: $catchPhrase")
            println("회사 비전: $bs")

            // 신용카드 번호 생성
            val creditCardNumber = faker.finance().creditCard()

            // 은행 계좌 번호 생성 (12자리 숫자)
            val bankAccountNumber = faker.number().digits(12)

            // 금액 생성
            val amount = faker.commerce().price() + "원"
        }
    }

    fun main() {
        val faker = Faker(Locale("ko"))
        val posts = mutableListOf<Post>()

        for (id in 1..10) {
            val post = Post(
                id = id,
                title = faker.book().title(),
                content = faker.lorem().paragraphs(3).joinToString("\n"),
                author = faker.name().fullName(),
                createdAt = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toString(),
                views = faker.number().numberBetween(0, 1000),
                commentsCount = faker.number().numberBetween(0, 100)
            )
            posts.add(post)
        }

        // 생성된 게시물 출력
        posts.forEach { post ->
            println("ID: ${post.id}")
            println("제목: ${post.title}")
            println("작성자: ${post.author}")
            println("작성일: ${post.createdAt}")
            println("조회수: ${post.views}")
            println("댓글 수: ${post.commentsCount}")
            println("내용:\n${post.content}")
            println("---------------")
        }
    }
}

data class Post(
    val id: Int,
    val title: String?,
    val content: String,
    val author: String?,
    val createdAt: String,
    val views: Int,
    val commentsCount: Int,
) {

}

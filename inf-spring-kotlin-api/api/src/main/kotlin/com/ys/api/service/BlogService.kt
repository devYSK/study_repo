package com.ys.api.service

import com.ys.api.blog.dto.BlogDto
import com.ys.api.blog.entity.Wordcount
import com.ys.api.blog.repository.WordRepository
import com.ys.api.core.exception.InvalidInputException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BlogService(
    val wordRepository: WordRepository
) {

    @Value("\${REST_API_KEY}")
    lateinit var restApiKey: String

    fun searchWordRank(): List<Wordcount> = wordRepository.findTop10ByOrderByCntDesc()

    @Transactional
    fun searchKakao(blogDto: BlogDto): String? {

        val lowQuery: String = blogDto.query.lowercase()
        val word: Wordcount = wordRepository.findById(lowQuery).orElse(Wordcount(lowQuery))
        word.cnt++
        wordRepository.save(word)


        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("/v2/search/blog")
                    .queryParam("query", blogDto.query)
                    .queryParam("sort", blogDto.sort)
                    .queryParam("page", blogDto.page)
                    .queryParam("size", blogDto.size)
                    .build()
            }
            .header("Authorization", "KakaoAK $restApiKey")
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()
        return result
    }

    private enum class ExceptionMsg(val msg: String) {
        EMPTY_QUERY("query parameter required"),
        NOT_IN_SORT("sort parameter one of accuracy and recency"),
        LESS_THAN_MIN("page is less than min"),
        MORE_THAN_MAX("page is more than max")
    }

}
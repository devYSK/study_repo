package com.ys.api.blog.repository

import com.ys.api.blog.entity.Wordcount
import org.springframework.data.repository.CrudRepository

interface WordRepository : CrudRepository<Wordcount, String> {

    fun findTop10ByOrderByCntDesc(): List<Wordcount>
}
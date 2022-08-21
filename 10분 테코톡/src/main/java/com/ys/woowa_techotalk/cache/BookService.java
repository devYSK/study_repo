package com.ys.woowa_techotalk.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author : ysk
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }


    @Cacheable(value = "book")
    public Book get(Long id) {
        return bookRepository.findById(id);
    }
}

package com.group.libraryapp.domain.book;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u0006H&J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\'\u00a8\u0006\n"}, d2 = {"Lcom/group/libraryapp/domain/book/BookRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/group/libraryapp/domain/book/Book;", "", "findByName", "bookName", "", "getStats", "", "Lcom/group/libraryapp/dto/book/response/BookStatResponse;", "library-app"})
public abstract interface BookRepository extends org.springframework.data.jpa.repository.JpaRepository<com.group.libraryapp.domain.book.Book, java.lang.Long> {
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.group.libraryapp.domain.book.Book findByName(@org.jetbrains.annotations.NotNull()
    java.lang.String bookName);
    
    @org.jetbrains.annotations.NotNull()
    @org.springframework.data.jpa.repository.Query(value = "SELECT NEW com.group.libraryapp.dto.book.response.BookStatResponse(b.type, COUNT(b.id)) FROM Book b GROUP BY b.type")
    public abstract java.util.List<com.group.libraryapp.dto.book.response.BookStatResponse> getStats();
}
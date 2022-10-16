package com.group.libraryapp.dto.book.response;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\u0006\u0010\u0015\u001a\u00020\u0016J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0019"}, d2 = {"Lcom/group/libraryapp/dto/book/response/BookStatResponse;", "", "type", "Lcom/group/libraryapp/domain/book/BookType;", "count", "", "(Lcom/group/libraryapp/domain/book/BookType;J)V", "getCount", "()J", "setCount", "(J)V", "getType", "()Lcom/group/libraryapp/domain/book/BookType;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "plusOne", "", "toString", "", "library-app"})
public final class BookStatResponse {
    @org.jetbrains.annotations.NotNull()
    private final com.group.libraryapp.domain.book.BookType type = null;
    private long count;
    
    @org.jetbrains.annotations.NotNull()
    public final com.group.libraryapp.dto.book.response.BookStatResponse copy(@org.jetbrains.annotations.NotNull()
    com.group.libraryapp.domain.book.BookType type, long count) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public BookStatResponse(@org.jetbrains.annotations.NotNull()
    com.group.libraryapp.domain.book.BookType type, long count) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.group.libraryapp.domain.book.BookType component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.group.libraryapp.domain.book.BookType getType() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long getCount() {
        return 0L;
    }
    
    public final void setCount(long p0) {
    }
    
    public final void plusOne() {
    }
}
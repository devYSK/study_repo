package com.group.libraryapp.dto.book.request;

public class BookLoanRequest {

  private String userName;
  private String bookName;

  public String getUserName() {
    return userName;
  }

  public String getBookName() {
    return bookName;
  }

  public BookLoanRequest(String userName, String bookName) {
    this.userName = userName;
    this.bookName = bookName;
  }
}

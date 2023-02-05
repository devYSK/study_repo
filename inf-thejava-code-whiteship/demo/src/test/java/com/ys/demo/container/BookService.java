package com.ys.demo;

import com.ys.demo.framework.Inject;

public class BookService {

	public BookRepository getBookRepository() {
		return bookRepository;
	}

	@Inject
	private BookRepository bookRepository;

}

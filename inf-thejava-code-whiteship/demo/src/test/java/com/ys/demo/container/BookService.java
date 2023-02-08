package com.ys.demo.container;

import com.ys.demo.framework.Inject;

public class BookService {

	public BookRepository getBookRepository() {
		return bookRepository;
	}

	@Inject
	private BookRepository bookRepository;

}

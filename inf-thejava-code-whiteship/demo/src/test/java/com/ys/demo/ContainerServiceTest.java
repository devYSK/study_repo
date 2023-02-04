package com.ys.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ys.demo.framework.ContainerService;

 class ContainerServiceTest {

	@Test
	 void getObject() {

		BookRepository bookRepository = ContainerService.getObject(BookRepository.class);

		assertNotNull(bookRepository);
	}


	@Test
	 public void getObject_BookService() {

		BookService bookService = ContainerService.getObject(BookService.class);

		assertNotNull(bookService.getBookRepository());
		assertNotNull(bookService);

	}
}

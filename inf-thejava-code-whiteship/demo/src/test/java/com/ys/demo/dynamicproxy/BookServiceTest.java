package com.ys.demo.dynamicproxy;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class BookServiceTest {

	BookService bookService =
		(BookService)Proxy.newProxyInstance(BookService.class.getClassLoader(),
			new Class[] {BookService.class},
			new InvocationHandler() {

				BookService bookService = new DefaultBookService();

				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

					if (method.getName().equals("rent")) {

						System.out.println("call");
						Object invoke = method.invoke(bookService, args);
						System.out.println("end");
					}
					return method.invoke(bookService, args);
				}
			}
		);
}
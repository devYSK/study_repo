package com.example;

import com.example.reactor.Publisher;

public class Main {
	public static void main(String[] args) {

		final Publisher publisher = new Publisher();

		publisher.startFlux()
				.subscribe(System.out::println);

		publisher.startMono().subscribe(System.out::println);
		System.out.println("Hello world!");
	}
}
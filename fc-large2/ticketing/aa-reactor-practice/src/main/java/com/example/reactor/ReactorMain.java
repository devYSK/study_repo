package com.example.reactor;

public class ReactorMain {
    public static void main(String[] args) {
        var publisher = new Publisher();

//        publisher.startFlux()
//                .subscribe(System.out::println);

        publisher.startMono2()
                .subscribe();
    }
}
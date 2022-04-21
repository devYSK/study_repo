package main

import (
	"fmt"
	"time"
)

func work1 (v chan int) {
	fmt.Println("Work1 : S --->", time.Now())
	time.Sleep(1 * time.Second)
	fmt.Println("Work1 : E --->", time.Now())
	v <- 1 // 1을 채널로 전송
}


func work2 (v chan int) {
	fmt.Println("Work2 : S --->", time.Now())
	time.Sleep(1 * time.Second)
	fmt.Println("Work2 : E --->", time.Now())
	v <- 2 // 1을 채널로 전송
}

func main() {
	fmt.Println("Main S", time.Now())

	v := make(chan int)

	go work1(v)
	go work2(v)

	<- v
	<- v

	//time.Sleep(2 * time.Second)
	fmt.Println("Main E -->", time.Now())
}

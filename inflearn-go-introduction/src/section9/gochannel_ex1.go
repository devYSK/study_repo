package main

import (
	"fmt"
	"time"
)

func main()  {

	c := make(chan int)

	go sendOnly(c, 10)
	go receiveOnly(c)

	time.Sleep(2 * time.Second)
}

func receiveOnly(c <- chan int) {
	for i := range c{
		fmt.Println("received : ", i)
	}

	fmt.Println(<- c)
}

func sendOnly(c chan <- int, cnt int) {
	for i:= 0; i < cnt; i++{
		c <- i
	}

	c <- 777
}

package main

import (
	"fmt"
)

func main()  {
	c := make(chan int)

	go rangeSum(1000, c)
	go rangeSum(700, c)
	go rangeSum(500, c)


	result1 := <- c
	result2 := <- c
	result3 := <- c

	fmt.Println(result1, result2, result3)

}

func rangeSum(rg int, c chan int) {
	sum := 0

	for i:=0; i <=rg; i++ {
		sum += i
	}

	c <- sum
}
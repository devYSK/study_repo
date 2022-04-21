package main

import "fmt"

func sum(cnt int) <- chan int {
	sum := 0
	total := make(chan int)
	go func() {
		for i:= 0; i < cnt; i++ {
			sum += i
		}
		total <- sum
	}()
	return total
}

func main() {

	// 채널 함수의 반환 값으로 사용

	c := sum(100)

	fmt.Println(c)

}

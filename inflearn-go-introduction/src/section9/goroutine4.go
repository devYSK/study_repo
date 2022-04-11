package main

import (
	"fmt"
	"time"
)

func main() {
	// 클로저 사용 예제

	s := "Goroutine Closure : "

	for i := 0; i < 1000; i++ {
		go func(n int) {
			fmt.Println(s, n, " - ", time.Now())
		}(i)
	}

	time.Sleep(3 * time.Second)
}

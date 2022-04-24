package main

import (
	"fmt"
	"runtime"
	"sync"
	"time"
)

func main()  {
	runtime.GOMAXPROCS(runtime.NumCPU())

	once := new(sync.Once)

	for i := 0; i< 5; i++ {
		go func(n int) {
			fmt.Println("Goroutine : ", n)
			once.Do(onceTest)
		}(i)
	}

	time.Sleep(2 * time.Second)
}

func onceTest() {
	// 한번 실행할 코드 작성
	fmt.Println("One!")
}

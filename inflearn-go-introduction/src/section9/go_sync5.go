package main

import (
	"fmt"
	"runtime"
	"sync"
	"time"
)

func main() {

	runtime.GOMAXPROCS(runtime.NumCPU())

	var mutex = new(sync.Mutex)
	var condition = sync.NewCond(mutex)

	c := make(chan int, 5) // 비동기 버퍼

	for i := 0; i < 5; i++ {
		go func(n int) {
			mutex.Lock()
			c <- 777
			fmt.Println("Goroutine Waiting", n)
			condition.Wait() // 고루틴 멈춤
			fmt.Println("Wating End : ", n)
			mutex.Unlock()
		}(i)
	}

	for i := 0; i < 5; i++ {
		<- c

		//fmt.Println("receieved : ", <- c)
	}

	for i:= 0; i < 5; i++ {
		mutex.Lock()
		fmt.Println("Wake Up")
		condition.Signal()
		mutex.Unlock()
	}

	time.Sleep(3 * time.Second)
}

package main

import (
	"fmt"
	"runtime"
	"sync"
)

type count struct {
	num int
	mutex sync.Mutex
}

func (c *count) increment() {
	c.mutex.Lock()
	c.num +=1
	c.mutex.Unlock()
}

func (c *count) result() {
	fmt.Println(c.num)
}

func main() {

	c := count{num : 0}
	done := make(chan bool)

	runtime.GOMAXPROCS(runtime.NumCPU())

	for i := 1; i <= 10000; i++ {
		go func() {
			c.increment()
			done <- true

			runtime.Gosched()
		}()
	}

	for i := 1; i<= 10000; i++ {
		<- done
	}

	c.result()
}

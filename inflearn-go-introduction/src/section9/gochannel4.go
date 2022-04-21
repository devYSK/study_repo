package main

import (
	"fmt"
	"runtime"
	"time"
)

func main() {

	// 예제2 (비동기 : 버퍼 사용)
	runtime.GOMAXPROCS(1)
	ch := make(chan bool, 2)

	cnt := 12

	go func() {
		for i:=0; i < cnt; i++ {
			ch <- true
			fmt.Println("Go Send: ", i)
			time.Sleep(1 * time.Second)
		}
	}()

	for i:= 0; i < cnt; i++ {
		fmt.Println("Main Receive", i,  <- ch)
	}

}

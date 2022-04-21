package main

import (
	"fmt"
	"time"
)

func main() {

	// 예쩨1 (동기 : 버퍼 미사용)

	ch := make(chan bool)

	cnt := 6

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

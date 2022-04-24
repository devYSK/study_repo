package main

import (
	"fmt"
	"sync"
)

func main()  {
	// 대기 그룹
	// 실행 흐름 변경(고루틴이 종료될 떄 까지 대기 기능)
	// WaiteGroup : Add() : 고루틴 추가, Done : 작업 종료 알림, Wait() : 고루틴 종료시까지 대기

	var cnt int64 = 0

	wg := new(sync.WaitGroup)

	for i := 0; i < 5000; i++ {
		wg.Add(1)
		go func(n int) {
			cnt+=1
			wg.Done()
		}(i)
	}

	for i:= 0; i < 2000; i++ {
		wg.Add(1)
		go func(n int) {
			cnt-=1
			wg.Done()
		}(i)
	}

	wg.Wait()

	fmt.Println(cnt)
}
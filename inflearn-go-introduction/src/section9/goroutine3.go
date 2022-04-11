package main

import "runtime"
import "fmt"
import "time"
import "math/rand"

func goroutineExe(name int) {
	r := rand.Intn(100) // 100미만의 랜덤한 수

	fmt.Println(name, " start ", time.Now())

	for i:= 0; i < 100; i++ {
		fmt.Println(name, " >>>>> ", r, i)
	}
}

func main() {


	fmt.Println("my max cpu : ", runtime.NumCPU())

	runtime.GOMAXPROCS(runtime.NumCPU())


	fmt.Println("Main Routine Start", time.Now())

	for i:= 0; i < 1; i++ {
		go goroutineExe(i)
	}

	time.Sleep(5 * time.Second)
}



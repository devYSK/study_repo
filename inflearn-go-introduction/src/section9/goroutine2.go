package main

import "fmt"

import "time"

func exe(name string) {
	fmt.Println(name, " start : ", time.Now())

	for i:=0; i < 1000; i++ {
		fmt.Println(name, ">>>>", i)
	}


	fmt.Println(name, " end : ", time.Now())
}

func main()  {

	exe("t1")

	fmt.Println("Main Routine Start", time.Now())

	go exe("t2")
	go exe("t3")

	time.Sleep(4 * time.Second)
}
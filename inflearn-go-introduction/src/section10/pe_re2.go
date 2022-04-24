package main

import "fmt"

func runFunc() {
	defer func() {
		s := recover()
		fmt.Println("Error Message : ", s)
	}()

	panic("Error ")
}

func main() {


	runFunc()

	fmt.Println("hello golang")

}

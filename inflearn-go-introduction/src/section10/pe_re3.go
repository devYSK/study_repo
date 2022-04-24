package main

import "fmt"

func runFunc2() {
	defer func() {
		if s := recover(); s!= nil {
			fmt.Println("Error Message : ", s)

		}
	}()

	//panic("Error ")

	a := [3]int{1, 2, 3}

	for i := 0; i < 5; i++ {
		fmt.Println("ex1 : ", a[i]) // 에러 발생
	}
}

func main() {


	runFunc2()

	fmt.Println("hello golang11")

}

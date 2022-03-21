package main

import (
	"fmt"
)

func main() {
	x := multiply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

	fmt.Println(x)
	fmt.Println(sum3(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

	slice1 := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}

	fmt.Println(sum3(slice1...)) // 슬라이스를 전달하는법

}

func multiply(n ...int) int {
	tot := 1
	for _, value := range n {
		tot *= value
	}

	return tot
}

func sum3(n ...int) int {
	tot := 0

	for i := range n {
		tot += i
	}
	return tot
}

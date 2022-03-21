package main

import "fmt"

func main() {

	//var arr [5]int
	var arr2 [5]int = [5]int{1, 2, 3, 4, 5}

	for i, v := range arr2 {
		fmt.Println(i, v)
	}

	//인덱스 생략
	for _, v := range arr2 {
		fmt.Println(v)
	}

	//인덱스 생략
	for v := range arr2 {
		fmt.Println(v)
	}

	// 배열 복사 - 값 복사
	arr3 := arr2

	fmt.Println(&arr2)
	fmt.Println(&arr3)

}

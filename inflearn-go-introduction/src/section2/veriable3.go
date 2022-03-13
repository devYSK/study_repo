package main

import "fmt"

func main() {
	// 짧은 선언
	// 함수 안에서만 사용(전역x) , 선언 후 할당 예외 발생

	shortVar1 := 3
	shortVar2 := "Test"
	shortVar3 := false

	fmt.Println("shortVar1 : ", shortVar1, "shortVar2 : ", shortVar2, "shortVar3 : ", shortVar3)
}

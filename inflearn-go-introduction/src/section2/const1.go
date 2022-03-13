package main

import "fmt"

func main()  {
	// 상수
	// const 사용하여 초기화. 선언과 동시 초기화.  한 번 선언 후 값 변경 금지. 고정된 값 관리 용

	const a string = "Test1"
	const b = "Test2"
	const c int32 = 10 * 10

	//const d = getHeight()

	fmt.Println(a, b, c)
}

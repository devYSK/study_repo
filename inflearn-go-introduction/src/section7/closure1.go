package main

import "fmt"

func main()  {

	multiply := func(x, y int) int {
		return x * y
	}

	r1 := multiply(5, 10)

	fmt.Println("ex1 : ", r1)

	m, n := 5, 10

	sum := func(c int) int {
		return m + n + c
	}

	r2 := sum(10)

	fmt.Println(r2)

	cnt := increaseCnt()

	fmt.Println(cnt)
}

func increaseCnt() func() int {
	n := 0
	return func() int {
		n += 1
		return n
	}
}


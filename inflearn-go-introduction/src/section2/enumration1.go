package main

import "fmt"

func main() {
	//열거형

	const (
		_ = iota
		A
		B
		C
	)

	fmt.Println(A, B, C)
}

package main

import (
	"fmt"
	"section12/arithmetic"
)

func main() {

	a := arithmetic.Numbers{100, 10}


	fmt.Println(a.Plus())

	fmt.Println(a.Multi())

	fmt.Println(a.Divide())

	fmt.Println(a.Minus())

	fmt.Println(a.SquareMinus())

	fmt.Println(a.SquarePlus())

	fmt.Println(a)
}


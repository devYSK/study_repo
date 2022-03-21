package main

import "fmt"

func main() {
	var a *int
	var b *int = new(int)

	i := 7

	a = &i
	b = &i

	fmt.Println("ex1 :", a, *a, &a)
	fmt.Println("ex1 :", b, *b, &b)

}

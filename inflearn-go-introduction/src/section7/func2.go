package main

import "fmt"

func multifly(x, y int) (int, int)  {
	return x * 10, y * 10;
}

func main()  {

	c, d := multifly(10, 10)

	fmt.Println(multifly(5, 5))
	fmt.Println(c, d)
}
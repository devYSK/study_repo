package main

import "fmt"

func main()  {

	for i := 0; i < 5; i++ {
		fmt.Println("ex1 : ", i)
	}

	loc := []string{"seoul", "busan", "incheon"}

	for index, name := range loc {
		fmt.Println("ex3 : ", index, name)
	}
}

package main

import "fmt"

func main() {
	a()
}

func a() {
	defer end(start("b"))

	fmt.Println("in a")
}

func end(s string) {
	fmt.Println("end : ", s)
}

func start(s string) string {
	fmt.Println("start : ", s)
	return s
}

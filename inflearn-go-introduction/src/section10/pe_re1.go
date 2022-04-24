package main

import "fmt"

func main()  {

	fmt.Println("Start Main")

	panic("Error occurred : user stopped!")

	fmt.Println("EndMain")
}
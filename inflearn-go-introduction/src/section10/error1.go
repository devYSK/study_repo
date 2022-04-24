package main

import (
	"log"
	"os"
)

func main()  {

	f, err := os.Open("notExists")

	if err != nil {
		log.Fatal(err.Error())
	}

	println(f.Name())

}
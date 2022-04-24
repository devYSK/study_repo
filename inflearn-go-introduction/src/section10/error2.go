package main

import (
	"fmt"
	"log"
)

func main()  {

	a, err := notZero(1)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("ex1 : ", a)

	b, err := notZero(0)

	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("ex2 : ", b)

}

func notZero(i int) (interface{}, interface{}) {
	if (i != 0) {
		s := fmt.Sprint("Hello Golang : ", i)
		return s, nil
	}

	return "", fmt.Errorf("%d 에러!", i)
}

package main

import (
	"fmt"
	"os"
)

func errorCheck1(e error) {
	if e != nil {
		panic(e)
	}
}

func errorCheck2(e error) {
	if e != nil {
		fmt.Println(e)
		return
	}
}

func main() {

	file, err := os.Create("./hi.txt")

	errorCheck1(err)

	defer file.Close()

	s1 := []byte{48, 49, 50, 51, 52}

	n1, err := file.Write([]byte(s1))

	errorCheck2(err)

	fmt.Printf("쓰기 작업 완료 %d \n", n1)

	s2 := "Hello Golang! \n File Write Test! -1 \n"

	n2, err := file.WriteString(s2)

	errorCheck1(err)

	fmt.Println(n2)

	file.Sync()
}

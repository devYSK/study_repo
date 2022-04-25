package main

import (
	"io/ioutil"
	"os"

)

func main() {
	s := "Hello Golang \n File Write Test \n"

	err := ioutil.WriteFile("test_write1.txt", []byte.(s), os.FileMode(0644)) // 권한(chmod). 앞에 0을 넣어준다.

	errorCheck1(err)

	data, err := ioutil.ReadFile("sample.txt")
}

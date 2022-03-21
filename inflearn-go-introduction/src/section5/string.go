package main

import (
	"fmt"
	"strings"
)

func main()  {

	str1 := "hi"
	str2 := "hi"
	strSet := []string{} // 슬라이스 연산
	strSet = append(strSet, str1)
	strSet = append(strSet, str2)

	fmt.Println(strings.Join(strSet, "-"))
}
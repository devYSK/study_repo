package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	// Switch 뒤 표현식 생략 가능
	// case 뒤 표현식 사용 가능
	// 자동 break 때문에 fallthrouth가 존재

	a := -7

	switch {
	case a < 0:
		fmt.Println(a, "는 음수")
	case a == 0:
		fmt.Println(a, "는 0")
	case a > 0:
		fmt.Println(" ", a, "는 양수")
	}

	switch b := 27; {
	case b < 0:
		fmt.Println(b, "는 음수")
	case b == 0:
		fmt.Println(b, "는 0")
	case b > 0:
		fmt.Println(" ", b, "는 양수")
	}

	switch c := "go"; c {
	case "go":
		fmt.Println("Go!")
	case "java":
		fmt.Println("Java!")
	default:
		fmt.Println("일치하는 값 없음")
	}

	switch c := "go"; c + "lang" {
	case "golang":
		fmt.Println("Go!")
	case "javalang":
		fmt.Println("Java!")
	default:
		fmt.Println("일치하는 값 없음")
	}

	switch i, j := 20, 30; {
	case i < j:
	case i == j:
	case i > j:
	}

	rand.Seed(time.Now().UnixNano())

	switch i := rand.Intn(100); { // 0부터 100까지의 랜덤
	case i >= 50 && i <= 100:
		fmt.Println("i -> ", i, " 50 이상 100 이하")
	case i >= 25 && i < 50:
		fmt.Println("i -> ", i, " 25 이상 50 미만")
	default:
		fmt.Println("i -> ", i, " 기본 값")
	}

	m := 30 / 15
	switch m {
	case 2, 4, 6:
	case 1, 3, 5:
	}

	switch e := "go"; e {
	case "java":
		fmt.Println("java!")
	case "go":
		fmt.Println("go!")
		fallthrough
	case "python":
		fmt.Println("python")
	case "ruby":
		fmt.Println("ruby")
	case "c":
		fmt.Println("c")
	}
	// break를 제외함
}

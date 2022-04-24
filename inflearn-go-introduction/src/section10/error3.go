package main

import (
	"fmt"
	"log"
	"math"
	"time"
)

type PowError struct {
	time time.Time	// 에러 발생 시간
	value float64	// 파라미터
	message string // 메시지
}

func (e PowError) Error() string {
	return fmt.Sprintf("[%v]Error - Input Value(value: %g) - %s", e.time, e.value, e.message)
}

func Power(f, i float64) (float64, error) {
	if f == 0 {
		return 0, PowError{time.Now(), f, "0은 사용할 수 없습니다"}
	}

	if math.IsNaN(f) {
		return 0, PowError{time.Now(), f, "숫자가 아닙니다. "}
	}

	if math.IsNaN(i) {
		return 0, PowError{time.Now(), i, "숫자가 아닙니다. "}
	}

	return math.Pow(f, i), nil
}

func main()  {

	v, err := Power(10, 3)

	if err != nil {
		log.Fatal(err)
	}

	fmt.Println(v)

	t, err := Power(10, 0)

	if err != nil {
		log.Fatal(err)
	}

	fmt.Println(t)

	a, err := Power(0, 0)

	if err != nil {

		fmt.Println(err.(PowError).time)
		fmt.Println(err.(PowError).value)
		fmt.Println(err.(PowError).message)

		log.Fatal(err)
	}

	fmt.Println(a)
}
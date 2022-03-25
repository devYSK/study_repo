package main

import (
	"fmt"
)

type Employee struct {
	name string
	salary float64
	bonus float64
}

type Executives struct {
	Employee
	specialBonus float64
}

func (e Employee) Calculate() float64 {
	return e.salary + e.bonus
}

func (e Executives) Calculate() float64 {
	return e.salary + e.bonus + e.specialBonus
}

func main() {
	// 구조체 임베디드 패턴. 다른 관점으로 메서드를 재 사용하는 장점 제공.
	// 상속을 허용하지 않는 Go언어에서 메소드 상속 활용을 위한 패턴

	ep1 := Employee{"kim", 2000000, 150000}
	ep2 := Employee{"pack", 1500000, 200000}

	ex := Executives{Employee{"lee", 5000000, 1000000}, 1000000}

	fmt.Println("ex1 :", int(ep1.Calculate()))
	fmt.Println("ex2 : ", int(ep2.Calculate()))
	fmt.Println("ex3 : ", int(ex.Calculate()) + int(ex.specialBonus)) //????
}
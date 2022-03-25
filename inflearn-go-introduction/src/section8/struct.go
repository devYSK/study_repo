package main

import (
	"fmt"
)

type Account struct {
	number string
	balance float64
	interest float64
}

func NewAccount(number string, balance float64, interest float64) * Account { // 포인터 반환 아닌 경우 값 복사
	return &Account{number, balance, interest} // 구조체 인스턴스를 생성한 뒤 리턴
}

func main()  {

	kim := Account{number: "245-901", balance: 10000, interest: 0.015}
	lee := Account{"245-902", 10000, 0.025}

	//park := NewAccount("245-903", 1000000, 134.134)

	CalculateD(kim)
	CalculateP(&lee)

	fmt.Println(kim.balance)
	fmt.Println(lee.balance)
}

func CalculateD(a Account) {
	a.balance = a.balance + (a.balance * a.interest)
}

func CalculateP(a *Account) {
	a.balance = a.balance + (a.balance * a.interest)
}



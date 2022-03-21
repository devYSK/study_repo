package main

import "fmt"

func main()  {
	say_one("1")
	sum2(100, add)

	a := 100
	multi_value(a)
	fmt.Println("ex2 : ", a)

	multi_referrence(&a)
	fmt.Println("ex2 :", a)
}

func say_one(m string) {
	fmt.Println("ex2 : ", m)
}

func sum2(i int, f func(int, int)){
	f(i, 10)
}

func add(a , b int) { // a, b 둘다 int형일때 생략 가능
	fmt.Println("ex1 :", a + b)
}

func multi_value(i int)  {
	i = i * 10
}

func multi_referrence(i *int)  {
	*i *= 10
}
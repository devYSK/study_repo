package main

import "fmt"

type Car struct {
	name string
	color string
	price int64
	tax int64
}

func Price(c Car) int64  { // 일반메서드
	return c.price + c.tax
}

func (c Car) Price() int64 { // 구조체 <-> 메서드 바인딩
	return c.price + c.tax
}

func main()  {
	bmw := Car{name: "520d", price : 50000000, color: "white", tax: 5000000}
	benz := Car{name: "220d", price : 60000000, color: "white", tax: 6000000}

	fmt.Println(bmw, bmw.price, bmw.tax)
	fmt.Println(benz.name, &benz)

	fmt.Println(Price(bmw))
	fmt.Println(bmw.Price())


	var k5 *Car = new(Car)
	k5.price = 50000000

	car1 := struct {
		name, color string
	}{"520d", "red"}

	fmt.Println(car1)
	fmt.Printf("%#v\n\n", car1)

	cars := []struct{name, color string} {
		{"520d", "red"},
		{"530i", "white"},
		{"528i", "blue"}}

	for _, c := range cars {
		fmt.Printf("(%s, %s) ----- (%#v)\n", c.name, c.color, c)
	}
}
package main

import "fmt"
import "reflect"
func main() {

	var a interface{} = 15

	b := a

	c := a.(int)

	//d := a.(float64) // 에러

	fmt.Println(reflect.TypeOf(a))
	fmt.Println(a, b, c)

}

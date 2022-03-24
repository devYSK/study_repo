package main

import "fmt"
import "reflect"

type Car2 struct {
	name string "차량명"
	color string "색상"
	company string "제조사"
	detail spec "상세"
}

type spec struct {
	length int "전장"
	height int "전고"
	width int "전축"
}

func main()  {

	tag := reflect.TypeOf(Car2{})

	for i:=0; i < tag.NumField(); i++ {
		fmt.Println(" ", tag.Field(i).Tag, tag.Field(i).Name, tag.Field(i).Type)
	}
}

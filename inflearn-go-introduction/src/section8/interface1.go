package main

import "fmt"

type test interface {

}

type Dog struct {
	name string
	weight int
}

//bite 메소드 구현
func (d Dog) bite() {
	fmt.Println(d.name, " 이 물었다!")
}

//동물의 행동 인터페이스 선언
type Behavior interface {
	bite()
}

func main() {

	var t test
	fmt.Println(t) // 빈(empty) 인터페이스인 경우 nil 리턴

	dog1 := Dog{"poll", 10}

	var inter1 Behavior
	inter1 = dog1
	inter1.bite()

	dog1.bite()

	dog2 := Dog{"marry", 12}
	inter2 := Behavior(dog2)

	inter2.bite()

	inters := []Behavior{dog1, dog2}

	for idx, _ := range inters {
		inters[idx].bite()
	}

	//값 형태로 실행(인터페이스)
	for _, val := range inters {
		val.bite()
	}
}
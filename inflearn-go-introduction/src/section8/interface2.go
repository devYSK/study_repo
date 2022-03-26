package main

import "fmt"

type Dog2 struct {
	name string
	weight int
}

type Cat struct {
	name string
	weight int
}

func (d Dog2) bite() {
	fmt.Println(d.name, " : Dog bites!")
}

func (d Dog2) sounds() {
	fmt.Println(d.name, " : Dog barks!")
}

func (d Dog2) run() {
	fmt.Println(d.name, " : Dog run!")
}

func (d Cat) bite() {
	fmt.Println(d.name, " : Cat bites!")
}

func (d Cat) sounds() {
	fmt.Println(d.name, " : Cat barks!")
}

func (d Cat) run() {
	fmt.Println(d.name, " : Cat run!")
}

type Behavior2 interface {
	bite()
	sounds()
	run()
}

func act(animal Behavior2) {
	animal.bite()
	animal.sounds()
	animal.run()
}

func main()  {
	dog := Dog2{"poll", 10}
	cat := Cat{"bob", 5}

	// 개 행동 실행
	act(dog)
	//고양이 행동실행
	act(cat)
}

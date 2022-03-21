package main

import "fmt"

func main()  {
	map4 := map[string] int{}
	map4["apple"] = 25
	map4["banana"] = 40
	map4["orange"] = 33

	map5 := map[string] int {
		"apple" : 15,
		"banana" : 40,
		"orange" : 23,
	}

	fmt.Println(map4)
	fmt.Println(map5)


}

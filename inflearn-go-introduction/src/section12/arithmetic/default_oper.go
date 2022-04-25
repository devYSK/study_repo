package arithmetic

// Numbers X,Y 2개의 Integer 구조
type Numbers struct {
	X int
	Y int
}

func (o *Numbers) Plus() int {
	return o.X + o.Y
}

func (o *Numbers) Minus() int {
	return o.X - o.Y
}

func (o *Numbers) Multi() int {
	return o.X * o.Y
}

func (o *Numbers) Divide() int {
	return o.X / o.Y
}



package arithmetic

// 두 숫자의 연산 계싼 제공 패키지

//x, y 제곱의 합을 리턴
func (o *Numbers) SquarePlus() int {
return (o.X * o.Y) + (o.X * o.Y)
}


//x, y 제곱의 차를 리턴
func (o *Numbers) SquareMinus() int {
return (o.X * o.Y) - (o.X * o.Y)
}


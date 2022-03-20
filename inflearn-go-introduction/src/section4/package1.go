// 패키지 1
package main
//선언방법2
import (
	"fmt"
	checkUp "section4/lib"
)

func main()  {


	fmt.Println("10보다 큰 수 ? : ", checkUp.CheckNum(10))

	//패키지는 코드 구조화(모듈화) 및 재사용
}
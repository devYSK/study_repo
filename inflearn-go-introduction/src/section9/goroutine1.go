package main


import "fmt"
import "time"

func exe1() {
	fmt.Println("exe1 func start", time.Now())
	time.Sleep(1 * time.Second)
	fmt.Println("exe1 func end", time.Now())
}

func exe2() {
	fmt.Println("exe2 func start", time.Now())
	time.Sleep(1 * time.Second)
	fmt.Println("exe2 func end", time.Now())
}

func exe3() {
	fmt.Println("exe3 func start", time.Now())
	time.Sleep(1 * time.Second)
	fmt.Println("exe3 func end", time.Now())
}

func main() {
	// 고루틴 : 타 언어의 쓰레드와 비슷한 기능을 함
	// 생성 방법 매우 간단, 리소스 매우 적게 사용.

	exe1()

	fmt.Println("Main routine Start", time.Now())

	go exe2()
	go exe3()

	time.Sleep(5 * time.Second)
	fmt.Println("main routine End", time.Now())

}


# 인프런 쉽고 빠르게 끝내는 GO언어 프로그래밍 핵심 기초 입문 과정

## https://mingrammer.com/gobyexample/

## EX) helloWorld
프로그램을 실행하기 위해, hello-world.go에 코드를 작성한 뒤 go run을 실행합니다.
```
$ go run hello-world.go
hello world
```
가끔은 프로그램을 바이너리로 빌드하고 싶을때가 있습니다. 이 땐 go build를 사용할 수 있습니다.
```
$ go build hello-world.go
$ ls
hello-world	hello-world.go
```
그런 다음 빌드된 바이너리를 직접 실행할 수 있습니다.

```
$ ./hello-world
```

hello world
이제 우리는 Go 프로그램을 실행하고 빌드할 수 있습니다, Go 언어에 대해 좀 더 배워봅시다.

## Go 장점 및 특징

* 간결한 문법 및 단순함
* 병행 프로그래밍 지원
* 정적 타입 및 동적 실행
* 간편한 협업 지원
* 컴파일 및 실행속도 빠름
* 제네릭 및 예외처리 미지원
* 컨벤션 통일

## 변수 및 상수

- 변수 선언 및 사용법 : var
- 상수 선언 및 사용법 : const

// 정수타입 : 0, 실수(소수점) : 0.0, 문자열 : " ", Boolean : true, false  
// 변수명 : 숫자 첫글자는 불가능. 대소문자 구분함.  문자 숫자 밑줄 특수기호 사용 가능.
```go
var a int
var b string
var c, d, e int
var f, g, h int = 1, 2, 3
var i float32 = 11.4
var j string = "Hi GOlang!"

f := "short"
```
* 변수를 선언해놓고 사용하지 않으면 빌드 에러.
* 초기화 없이 선언된 변수는 제로값.  예를 들어, int의 제로값은 0
* := 문법은 변수를 선언하는 동시에 초기화하기 위한 단축 표현식입니다. 예를 들면 이 경우는 var f string = "short"를 뜻합니다.

```go

func main() {
	//여러개 선언
	var (
		name string = "machine"
		height int32
		weight float32
		isRunning bool
	)

	height = 250
	weight = 350.56
	isRunning = true

	fmt.Println("name : ", name, "height : ", height, "weight : ", weight, "isRunning :", isRunning)
	
}
```
* 위처럼 여러변수를 하나로 모아서 선언해서 사용도 가능

### 짧은 선언
```go
// 짧은 선언
shortVar1 := 3
shortVar2 := "Test"
shrotVar3 := false

shortVar1 := 99 // 에러!! 재할당 불가 
```

* 이 스코프 안에서만 1회성으로 사용하는것 이 메서드가 끝나면 메모리에서 사라짐 
* 함수 내에서 초기화하여 할당한 변수는 재 할당을 할 수 없다 

### 상수
```go

package main
import "fmt"
import "math"

const s string = "constant"

func main() {
	// 상수
	// const 사용하여 초기화. 선언과 동시 초기화.  한 번 선언 후 값 변경 금지. 고정된 값 관리 용

	const a string = "Test1"
	const b = "Test2"
	const c int32 = 10 * 10

	//const d = getHeight()
	
	fmt.Println(s)

	const n = 500000000

	const d = 3e20 / n
	fmt.Println(d)
	fmt.Println(int64(d))
	fmt.Println(math.Sin(n))
}

```
* const로 상수값을 선언합니다.
* const문은 var문과 동일하게 사용할 수 있습니다.
* 상수 표현식은 임의의 정밀도로 산술 연산을 수행합니다.

* 숫자 상수는 명시적 캐스팅등으로 타입이 주어지기 전까진 타입을 가지지 않습니다.

* 숫자는 변수 할당이나 함수 호출과 같은 컨텍스트에서 사용하여 타입을 부여할 수 있습니다. 예를 들면, math.Sin은 float64를 기대합니다.

## 열거형 (Enumeration)
* `Iota`
* 주로 상수를 사용하는 일정한 규칙에 따라 숫자를 계산 하는 묶음 
* 다음 변수부터 1씩 증가함
```go
func main() {
	//열거형
	const (
		A = iota // 0
		B        // 1
		C        // 2
	)

	fmt.Println(A, B, C)
}
```
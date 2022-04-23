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

## if 문
```go

func main() {

	b := 0

	// 에러
	if b >= 25
	{
		fmt.Println("b > 25 ")
	}

	// 에러
	if b >= 25
		fmt.Println("b > 25")

	// 정상
	if b >= 25 {
		fmt.Println("b > 25")
	}

	//에러
	if c:=1; c {
		fmt.Println("true")
	}

	// 정상
	if c :=40; c >= 35 { // c의 스코프는 if문 안에서만임.
		fmt.Println("35이상")
	}

	var a int = 50

    b := 70

    // 예제1
    if a >= 65 {
        fmt.Println("65 이상")
    } else {
        fmt.Println("65 이하")
    }

    // 예제2
    if b >= 65 {
        fmt.Println("65 이상")
    } else {
        fmt.Println("65 이하")
    }

	i := 100


	if i >= 120 {

	} else if i >= 100 && i< 120 {
		
	} else if i < 100 && i >= 50 {
		
	} else {

}

}
```

## switch-case문

```go
package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	// Switch 뒤 표현식 생략 가능
	// case 뒤 표현식 사용 가능
	// 자동 break 때문에 fallthrouth가 존재

	a := -7

	switch {
	case a < 0:
		fmt.Println(a, "는 음수")
	case a == 0:
		fmt.Println(a, "는 0")
	case a > 0:
		fmt.Println(" ", a, "는 양수")
	}

	switch b := 27; {
	case b < 0:
		fmt.Println(b, "는 음수")
	case b == 0:
		fmt.Println(b, "는 0")
	case b > 0:
		fmt.Println(" ", b, "는 양수")
	}

	switch c := "go"; c {
	case "go":
		fmt.Println("Go!")
	case "java":
		fmt.Println("Java!")
	default:
		fmt.Println("일치하는 값 없음")
	}

	switch c := "go"; c + "lang" {
	case "golang":
		fmt.Println("Go!")
	case "javalang":
		fmt.Println("Java!")
	default:
		fmt.Println("일치하는 값 없음")
	}

	switch i, j := 20, 30; {
	case i < j:
	case i == j:
	case i > j:
	}

	rand.Seed(time.Now().UnixNano())

	switch i := rand.Intn(100); { // 0부터 100까지의 랜덤
	case i >= 50 && i <= 100:
		fmt.Println("i -> ", i, " 50 이상 100 이하")
	case i >= 25 && i < 50:
		fmt.Println("i -> ", i, " 25 이상 50 미만")
	default:
		fmt.Println("i -> ", i, " 기본 값")
	}

	m := 30 / 15
	switch m {
	case 2, 4, 6:
	case 1, 3, 5:
	}
	switch e := "go"; e {
	case "java":
		fmt.Println("java!")
	case "go":
		fmt.Println("go!")
		fallthrough
	case "python":
		fmt.Println("python")
	case "ruby":
		fmt.Println("ruby")
	case "c":
		fmt.Println("c")
	}
	// break를 제외함
}
```
* Go는 기본적으로 switch에서 break 사용 안해도 작동함
* fallthrough는 break를 무시하는 방법. 다음 케이스들도 진행한다. 

## for

* 기본적으로 python이나 다른 언어의 문법과 유사함
* 다중 포문일시에 break 사용만 주의할 것.
  * 가장 안쪽 for만 벗어나므로 Loop1: for { for { } } 식으로 정의해놓고 break Loop1 식으로 사용
```go
package main

import "fmt"

func main()  {

	for i := 0; i < 5; i++ {
		fmt.Println("ex1 : ", i)
	}

	loc := []string{"seoul", "busan", "incheon"}

	for index, name := range loc {
		fmt.Println("ex3 : ", index, name)
	}
}
```

## 패키지 (Package)
```go
// 패키지 1
package main

// 선언방법1

import "fmt"
import "os"

//선언방법2
import (
	"fmt"
	"os"
)
```
* Go : 패키지 단위의 독립적으로 작은단위로 개발할 것을 권고
* 패키지 이름 = 디렉토리 이름
* 같은 패키지 내 -> 소스파일들은 디렉토리명을 패키지 명으로 사용한다. 
* 네이밍 규칙 : `소문자 private / 대문자 public`
  * private : 패키지 외부에서 접근 불가
  * public : 패키지 내에서만 접근 가능 



* `기본 패키지는 GOROOT에 있다.`
* main package는 특별하게 인식된다 -> main은 1개여야함. -> 컴파일러는 공유 라이브러리가 아니라 프로그램의 시작점 


* package section4/lib is not in GOROOT 가 나오면 `go mod init` 명령어 사용 
  * https://sean-ma.tistory.com/51

```go
// 패키지 1
package main
//선언방법2
import (
	"fmt"
	"section4/lib"
)

func main()  {
	fmt.Println("10보다 큰 수 ? : ", lib.CheckNum(10))
}
//// section4/lib
package lib

func CheckNum(c int32) bool {
  return c > 10
}

```
* import를 사용해서 다른 패키지의 코드 재사용. 

* `alias`를 사용하여 패키지 별칭으로도 사용 가능하다. 
```go
import (
	"fmt"
	checkUp "section4/lib"
)

func main()  {
	fmt.Println("10보다 큰 수 ? : ", checkUp.CheckNum(10)) // 별칭명
}
```

## 초기화(init) 함수

* init 함수는 패키지 로드시에 가장 먼저 호출된다.
* 초기화 되는 작업 작성시 유용
* 여러개의 init() 메서드가 있을 경우 먼저 선언한 순서대로(위에서 아래) 호출된다.
* ![](img/d0a12f59.png)
```go
func init()  {
	fmt.Println("Init Start")
}

func main()  {
	fmt.Println("Main Start")
}
```
* init()이 먼저 호출됨.

# Go 데이터 타입 (자료형)

## Bool(Boolean)
* 암묵적 형변환이 안된다. 0,Nil -> false가 아님.
  * false는 false로만 써야함
  * 1 은 true가 아님 0도 false가 아님.

## Numeric(1)

* 정수, 실수, 복소수
* 32bit, 64bit, unsigned
* 8진수(0) 16진수(0x)
* float32
* https://go.dev/ref/spec#Numeric_types

## 숫자형 연산
* https://pkg.go.dev/math
* 타입이 같아야 연산 가능하다
* 다른 타입끼리는 반드시 형 변환 후 연산 (없을 경우 예외 발생) (여기서 연산은 +, -, *, % /, <<, >>, &, ^ 
  * ex ) int16(10000) + uint8(100) = 에러 발생 / 다른 타입임

## 문자열

* Go는 char 타입이 존재하지 않음 -> rune(int32)
* "", `` 두가지 존재
* 문자 : ''
* 자주사용하는 escape : \\, \', \"
```go
var str1 string = "c:\\go\\src" // 둘다 같다
str2 := `c:\go\src`             // 둘다 같다
```
* len(str) : 문자열 길이. 바이트 갯수.
  * 실제길이 : len([]rune(str))

## 문자열 연산
* 문자열로 비교를 하게되면 아스키 코드에 대한 사전식 비교를 함 
```go
str1 := "Golang"
str2 := "World"

str1 == str2 // false
str1 != str2 // true
str1 > str2 // false
str1 < str2 // true

```

```go

func main()  {

	str1 := "hi"
	str2 := "hi"
	strSet := []string{} // 슬라이스 연산
	strSet = append(strSet, str1)
	strSet = append(strSet, str2)
	
	fmt.Println(strings.Join(strSet, "-"))
}
```
* 문자열에서 + 연산보다 strings.Join이 성능이 훨씬 좋다. (Java의 StringBuffer처럼)

# 배열, 슬라이스, 맵

## 배열
* 배열은 용량, 길이 항상 같다
* 길이 고정
* cap() : 배열, 슬라이스 용량
* len() : 배열, 슬라이스 개수
```go
func main()  {
	var arr [5]int
	var arr2 [5]int = [5]int{1, 2, 3, 4, 5}
    var arr3 = [5]int{1, 2, 3, 4, 5}
}
```
* 초기화가 안된 값은 0으로 초기화된다

* 다음과 같이 for문으로 사용
```go
func main() {

	var arr [5]int
	var arr2 [5]int = [5]int{1, 2, 3, 4, 5}

	for i, v := range arr2 {
		fmt.Println(i, v)
	}

	//인덱스 생략
	for _, v := range arr2 {
		fmt.Println(v)
	}

	//인덱스 생략
	for v := range arr2 {
		fmt.Println(v)
	}
    for i := 0; i < 2; i++ {
        for j := 0; j < 3; j++ {
            arr[i][j] = i + j
        }
    }
}
```

## 슬라이스

* 배열과 비슷하지만, 길이가 가변적이다.
* 참조 타입(레퍼런스)
* 길이가 0인 빈 배열을 만들기 위해선 내장 함수 make를 사용
  * > var slice []int 
  * 이렇게도 선언 가능
```go
arr := make([]string, 3)
```

* 새로운 슬라이스를 사용하기위해선 append로부터 반환되는 값을 사용
```go
arr := make([]int, 3) // 길이가 3인 정수형 배열
arr = append(arr, 5) // 원래 변수에 반환받아야함. -> 병합 후 리턴
```

* 정렬은 sort패키지
  * golang.org/pkg/sort


## Map (해시 또는 딕셔너리)
* 레퍼런스 타입
* key - value로 저장. 
* key로는 레퍼런스(참조)타입 불가. 기본타입만.
  * 값(value)으로는 모든 타입 사용 가능 
```go
var map1 map[string] int = make(map[string] int)
var map1 = make(map[string] int)
map1 := make(map[string] int)
map1 := map[string] int{}
```
* 4가지 방법 모두 같다.
* make 사용 make(map[key-type]val-type)
* name[key] = val로 key/value 쌍을 저장
```go
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

    value3, is = map5["kiwi"]
    fmt.Println(value3, is) // 0 false
}
```
* len을 맵에 사용하면 key/value 쌍의 갯수반환
  * len(map4)
* delete 내장 함수는 맵의 key/value 쌍을 삭제
  * delete(map4, "apple")
* key 값의 존재를 확인하는법
```go
value3, is = map5["kiwi"]
fmt.Println(value3, is) // 0 false

if value, key := map5["kiwi"]; key {
	fmt.Println("존재")
} else {
	fmt.Println("존재하지 않음.")
}
```
* 두번째 값으로 키가 존재하는지 알 수 있다.
  * 존재하면 true 존재하지 않으면 false

## 포인터

* *(애스터리스크로 사용)
* 주소의 값은 직접 변경 불가능
* new로 초기화 하지 않으면, nil로 초기화 
* 초기화 방법 
```go
var a *int
var b *int = new(int)

a = &i
b = &i

fmt.Println("ex1 :", a, *a)
fmt.Println("ex1 :", b, *a)
```
> ex1 : 0x14000130010 7  
  ex1 : 0x14000130010 7
* *로 변수를 감싸면 값이 나오고, 그냥 변수만 출력하면 주솟값이 나온다. 

# 함수
* https://go.dev/ref/spec#Function_types

* 선언 : func 키워드
  * func 함수명(매개변수) (반환타입 or 반환 값 변수명)
* 반환값이 존재하지 않을경우
  * func 함수명(매개변수)
* 타 언어와 달리 반환 값(return value) 복수 사용 가능 
* 함수도 전달할 수 있다.
* 콜백 전달, 참조 전달, 값 전달
```go

func main()  {
	say_one("1")
	sum(100, add)
}

func say_one(m string) {
	fmt.Println("ex2 : ", m)
}

func sum(i int, f func(int, int)){
	f(i, 10)
}

func add(a , b int) { // a, b 둘다 int형일때 생략 가능
	fmt.Println("ex1 :", a + b)
}
```
* 함수 전달 패턴. 

* reference by value (값 참조)
```go
func multi_referrence(i *int)  {
	*i *= 10
}
```
* 이렇게 포인터를 이용하면 참조해서 함수 외부의 변수 값도 바꿀 수 있다.

* 여러 리턴 값
```go
func multifly(x, y int) (int, int)  {
	return x * 10, y * 10;
}

func main()  {

	c, d := multifly(10, 10)

	fmt.Println(multifly(5, 5))
	fmt.Println(c, d)
}
```
* 여러개의 리턴 값을 받을 수 있다.

### 함수 고급

* 가변인자 
  * 슬라이스도 전달 가능하다

```go
func main() {
	x := multiply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

	fmt.Println(x)
	fmt.Println(sum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

	slice1 := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}

	fmt.Println(sum(slice1...)) // 슬라이스를 전달하는법
}

func multiply(n ...int) int {
	tot := 1
	for _, value := range n {
		tot *= value
	}

	return tot
}

func sum(n ...int) int {
	tot := 0

	for i := range n {
		tot += i
	}
	return tot
}
```

* 슬라이스 매개변수로 전달방법 : func(slice...)
* 함수를 변수처럼 할당할 수 있다. 
```go
f := []func(int, int) int{multiply, sum}
	
a := f[0](10, 10) // a := multiply(10,10) 이랑 같음
b := f[1](5, 5) // b := sum(5,5) 랑 같음 
```

### 익명함수
```go
func main() {
	func() {
          fmt.Println("익명함수")
        }()
}
```
## Defer
* 흐름을 제어하기 위해 고에서 제공
* Defer 함수 실행(지연)
* Defer를 호출한 함수가 종료되기 직전에 호출 된다. 
* 타언어의 Finally 문과 비슷
* 주로 리소스 반환(DB 커넥션, File 닫기, mutex 잠금 해제)
```go
package main

import "fmt"

func ex_f1() {
	fmt.Println("f1 : start!")
	defer ex_f2() // 마지막에 호출
	fmt.Println("f1 : end!")
}

func ex_f2() {
	fmt.Println("f2 : called!")
}

func main()  {
	ex_f1()
}
```
* 결과
> f1 : start!  
f1 : end!  
f2 : called!  

* Stack구조 후입 선출 이라고 생각하면 된다.

```go

func main() {
	a()
}

func a() {
	defer end(start("b"))
	fmt.Println("in a")
}

func end(s string) {
	fmt.Println("end : ", s)
}

func start(s string) string {
	fmt.Println("start : ", s)
	return s
}
```
* 결과
> start :  b  
in a  
end :  b  

* defer문은 defer문을 붙인 함수만 적용된다.
  * 즉 end() 함수만 지연시키고, start() 함수는 먼저 실행한다.

## Closure (클로져)

* 익명함수 사용할 경우 함수를 변수에 할당해서 사용 가능하게 하는것
* 함수 안에서 함수를 선언 및 정의 가능 -> 외부 함수에 선언된 변수에 접근 가능한 함수 
* 함수가 선언되는 수간에 함수가 실행 될 때 실체의 외부 변수에 접근하기 위한 스냅샷
* 함수를 호출할 때 이전에 존재했던 값을 유지하기 위해서 

* Closure는 함수 바깥에 있는 변수를 참조하는 함수값(function value)를 일컫는데, 이때의 함수는 바깥의 변수를 마치 함수 안으로 끌어들인 듯이 그 변수를 읽거나 쓸 수 있게 된다.

```go
func main() {
    m, n := 5, 10
    sum := func(c int) int {
        return m + n + c
    }
    r2 := sum(10)
    fmt.Println(r2)
}
```

```go

func main() {
	cnt := increaseCnt()
	fmt.Println(cnt)
}

func increaseCnt() func() int {
    n := 0
    return func() int {
        n += 1
    return n
    }
}
```
* 내부 함수 func() int의 입장에서 외부 변수 n를 참조하여 호출할때마다 증가.
* cnt2 := increaseCnt()와 같이 새로운 Closure함수를 생성한다면 변수n은 초기 0 을 갖게된다. 

# Go 객체 지향 

## 사용자 정의 타입

* Go에서는 객체지향 타입을 구조체로 정의한다.
* 상속, 클래스 개념이 없다.
* 객체지향의 특징을 가지고 있지 않지만, 인터페이스 -> 다형성 지원
  * 구조체를 클래스 형태로 개발 가능.

* struct는 필드 데이타만을 가지며, (행위를 표현하는) 메서드를 갖지 않는다.
Go 언어는 객체지향 프로그래밍(Object Oriented Programming, OOP)을 고유의 방식으로 지원한다. 즉, Go에는 전통적인 OOP 언어가 가지는 클래스, 객체, 상속 개념이 없다. 전통적인 OOP의 클래스(class)는 Go 언어에서 Custom 타입을 정의하는 struct로 표현되는데, 전통적인 OOP의 클래스가 필드와 메서드를 함께 갖는 것과 달리 Go 언어의 struct는 필드만을 가지며, 메서드는 별도로 분리하여 정의한다.

```go
type Car struct {
	name string
	color string
	price int64
	tax int64
}

func Price(c Car) int64  { // 일반메서드
	return c.price + c.tax
}

func (c Car) Price() int64 { // 구조체 <-> 메서드 바인딩
	return c.price + c.tax
}

func main()  {
	bmw := Car{name: "520d", price : 50000000, color: "white", tax: 5000000}
	benz := Car{name: "220d", price : 60000000, color: "white", tax: 6000000}

	fmt.Println(bmw, bmw.price, bmw.tax)
	fmt.Println(benz.name, &benz)

	fmt.Println(Price(bmw))
	fmt.Println(bmw.Price())
}
```

* struct를 정의하기 위해서는 Custom Type을 정의하는데 사용하는 type 문을 사용

* struct 필드값을 순서적으로 { } 괄호안에 넣을 수 있으며, 순서에 상관없이 필드명을 지정하고(named field) 그 값을 넣을 수 도 있다. 
* 필드명을 지정하는 경우, 만약 일부 필드가 생략될 경우 생략된 필드들은 Zero value (정수인 경우 0, float인 경우 0.0, string인 경우 "", 포인터인 경우 nil 등)를 갖는다.

* 앞에 &를 붙이면 구조체 포인터를 생성

* 닷(.)을 사용해 구조체 필드에 접근
* 구조체 포인터에서도 닷(.)을 사용할 수 있습니다. 이 때 포인터는 자동으로 역참조됩니다.
* 구조체는 수정이 가능(mutable)합니다.

```go
var bmw = new(Car)
k5 := new(Car)

var k5 *Car = new(Car) // 중요
k5.price = 50000000
```

* 또 다른 객체 생성 방법으로 Go 내장함수 new()를 쓸 수 있다. 
* new()를 사용하면 모든 필드를 Zero value로 초기화하고 person 객체의 포인터(*person)를 리턴한다. 
* 객체 포인터인 경우에도 필드 엑세스 시 . (dot)을 사용하는데, 이 때 포인터는 자동으로 Dereference 된다

* `인터페이스 메소드를 선언만 해둔 후 오버라이딩 해서 메서드에 포인터 리시버를 사용할 경우 반드시 &struct`


```go
package main
 
type dict struct {
    data map[int]string
}
 
//생성자 함수 정의
func newDict() *dict {
    d := dict{}
    d.data = map[int]string{}
    return &d //포인터 전달
}
 
func main() {
    dic := newDict() // 생성자 호출
    dic.data[1] = "A"
}
```


* 생성자(constructor) 함수
* 구조체(struct)의 필드가 사용 전에 초기화되어야 하는 경우struct 의 필드가 map 타입인 경우 map을 사전에 미리 초기화해 놓으면, 외부 struct 사용자가 매번 map을 초기화 해야 된다는 것을 기억할 필요가 없다. 

* 익명구조체
```go
func main() {
    car1 := struct {name, color string}{"520d", "red"}
    
    fmt.Println(car1)
    fmt.Printf("%#v\n\n", car1)
    
    cars := []struct{name, color string} {
        {"520d", "red"},
        {"530i", "white"},
        {"528i", "blue"}}
    
    for _, c := range cars {
        fmt.Printf("(%s, %s) ----- (%#v)\n", c.name, c.color, c)
    }
}
```

* reflection으로 구조체 정보 가져오기
```go
package main

import "fmt"
import "reflect"

type Car2 struct {
	name string "차량명"
	color string "색상"
	company string "제조사"
}

func main()  {
	tag := reflect.TypeOf(Car2{})

	for i:=0; i < tag.NumField(); i++ {
		fmt.Println(" ", tag.Field(i).Tag, tag.Field(i).Name, tag.Field(i).Type)
	}
}
```

* 구조체 내부에 구조체를 갖는 구조도 가능
```go
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
```

## 구조체 심화
```go
type Account struct {
	number string
	balance float64
	interest float64
}

func NewAccount(number string, balance float64, interest float64) * Account { // 포인터 반환 아닌 경우 값 복사
	return &Account{number, balance, interest} // 구조체 인스턴스를 생성한 뒤 리턴
}

func main()  {

	kim := Account{number: "245-901", balance: 10000, interest: 0.015}
	lee := Account{"245-902", 10000, 0.025}

	//park := NewAccount("245-903", 1000000, 134.134)
	
	CalculateD(kim)
	CalculateP(&lee)

	fmt.Println(kim.balance)
	fmt.Println(lee.balance)
}

func CalculateD(a Account) {
	a.balance = a.balance + (a.balance * a.interest)
}

func CalculateP(a *Account) {
	a.balance = a.balance + (a.balance * a.interest)
}
```

* NewAccount 메서드는 생성자가 되는것. 
* *형으로 받으면 &형으로 전달해줘야 한다. 

## 구조체 메소드 상속 패턴 - 구조체 임베디드 패턴
```go
type Employee struct {
	name string
	salary float64
	bonus float64
}

func (e Employee) Calculate() float64 {
	return e.salary + e.bonus
}

type Executives struct {
	Employee
	specialBonus float64
}

func main() {
	// 구조체 임베디드 패턴. 다른 관점으로 메서드를 재 사용하는 장점 제공.
	// 상속을 허용하지 않는 Go언어에서 메소드 상속 활용을 위한 패턴

	ep1 := Employee{"kim", 2000000, 150000}
	ep2 := Employee{"pack", 1500000, 200000}

	ex := Executives{Employee{"lee", 5000000, 1000000}, 1000000}
	
	fmt.Println("ex1 :", int(ep1.Calculate()))
	fmt.Println("ex2 : ", int(ep2.Calculate()))
	fmt.Println("ex3 : ", int(ex.Calculate()) + int(ex.specialBonus)) // <<<<< 
}
```
* 바로호출할 수 있다. 

## 구조체 메소드 오버라이딩 패턴 

```go
type Employee struct {
	name string
	salary float64
	bonus float64
}

type Executives struct {
	Employee
	specialBonus float64
}

func (e Employee) Calculate() float64 {
	return e.salary + e.bonus
}

func (e Executives) Calculate() float64 { // 오버라이딩
	return e.salary + e.bonus + e.specialBonus
}
```
* 같은 이름을 가진 메서드이지만 사용 가능.

# 인터페이스 기초

* 객체의 동작을 표현, 골격. 
* 동작에 대한 방법만 표시. 
* 추상화 제공
* 인터페이스의 메소드를 구현한 타입은 인터페이스로 사용 가능 
* 덕타이핑 : Go언어의 독창적인 특성 
* 클래스간의 결합도 감소 -> 유지보수성 향상, 개발 추가의 용이
* 인터페이스 사용법 2가지
* ``` go
   type 인터페이스명 interface {
    메서드1() 반환값 (타입형)
    메서드2() // 반환값 없을 경우
   }
   ```
  
```go
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
``` 
* 이렇게도 가능하다
```go
type Dog struct {
	name string
	weight int
}

type Cat struct {
	name string
	weight int
}

func (d Dog) bite() {
	fmt.Println(d.name, " : Dog bites!")
}

func (d Dog) sounds() {
	fmt.Println(d.name, " : Dog barks!")
}

func (d Dog) run() {
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

type Behavior interface {
	bite()
	sounds()
	run()
}

func act(animal Behavior) {
	animal.bite()
	animal.sounds()
	animal.run()
}

func main()  {
	dog := Dog{"poll", 10}
	cat := Cat{"bob", 5}

	// 개 행동 실행
	act(dog)
	//고양이 행동실행
	act(cat)
}
```


## 인터페이스 고급

* 빈(empty) 인터페이스 활용
* 함수 매개변수, 리턴 값, 구조체 필드 등으로 사용 가능 - > 어떤 타입으로도 변환 가능 
* 모든 타입을 나타내기 위해 빈 인터페이스 사용
* 동적 타입이라고 생각하면 쉽다. 

```go
func printContents(v interface{}) {
	fmt.Printf("Type : (%T)", v) // 원본 타입
	fmt.Println("ex1 ", v)
}
```
### 타입 변환
* 실행(런타임) 시에는 인터페이스에 할당한 변수는 실제 타입으로 변환 후 사용해야 하는 경우에는? 
  * 형변환 : 인터페이스.(타입)
  * interfaceValue.(type)

# 고루틴(goroutine)

* Go루틴(goroutine)은 Go 런타임이 관리하는 Lightweight 논리적 (혹은 가상적) 쓰레드(주1)이다. Go에서 "go" 키워드를 사용하여 함수를 호출하면, 런타임시 새로운 goroutine을 실행

* OS 쓰레드 보다 훨씬 가볍게 비동기 Concurrent 처리를 구현하기 위하여 만든 것.
  * Go 런타임이 자체 관리. 
* 메모리 측면에서도 몇 킬로 바이트의 스택을 가짐(필요시 동적으로 증가)
  * 리소스 매우 적음

* 비동기적 함수 루틴 실행 
  * Go 채널을 통하여 통신

* 공유메모리 사용 시에 정확한 동기화 코딩 필요.

* 메인이 죽으면 다른 루틴도 끝난다.

* `다중 CPU 처리`
  * runtime.GOMAXPROCS(코어수)
    * 내 컴퓨터의 코어 수 : runtime.NumCPU()

* 반복문 클로저는 일반적으로 즉시 실행
* 고루틴 클로저는 가장 나중에 실행(반복문 종료 후 막 실행)

# Go 채널

* GoRoutine 간의 상호 정보 교환 및 실행 흐름 동기화 위해 사용
* 실행 흐름 제어 가능(동기, 비동기) -> 일반 변수로 선언 후 사용 가능
* interface{} 전달을 통해서 자료형 상관없이 전송 및 수신 가능.

* Go 채널은 그 채널을 통하여 데이타를 주고 받는 통로라 볼 수 있는데, 채널은 make() 함수를 통해 미리 생성되어야 하며, 채널 연산자 <- 을 통해 데이타를 보내고 받는다. 채널은 흔히 goroutine들 사이 데이타를 주고 받는데 사용되는데, 상대편이 준비될 때까지 채널에서 대기함으로써 별도의 lock을 걸지 않고 데이타를 동기화하는데 사용된다.

* <-, -> 사용. (채널 <- 데이터, 데이터 <- 채널) 
  * 채널로 전송, 채널에서 수신

* 채널은 데이터가 올때까지 대기함.
  * time.Sleep() 메서드로 기다릴 필요가 없음.

```go
func main()  {
	c := make(chan int)

	go rangeSum(1000, c)
	go rangeSum(700, c)
	go rangeSum(500, c)
	
	result1 := <- c
	result2 := <- c
	result3 := <- c

	fmt.Println(result1, result2, result3)
}

func rangeSum(rg int, c chan int) {
	sum := 0
	for i:=0; i <=rg; i++ {
		sum += i
	}

	c <- sum
}
```

* 순서대로 데이터 수신 : 채널에서 값 수신 완료 될 때 까지 대기 

* 채널에서 버퍼 사용
  * ch = make(chan bool, 2)

* make(chan type, N) 함수를 통해 생성.  두번째 파라미터 N에 사용할 버퍼 갯수

* 버퍼 : 발신 -> 가득차면 대기, 비어있으면 작동, 수신 -> 비어있으면 대기, 가득 차있으면 작동 

* 채널을 사용하면 반드시 닫아줘야 한다. 

* close()
* 채널 수신에 사용되는 <- ch 은 두개의 리턴값을 갖는데, 첫째는 채널 메시지이고, 두번째는 수신이 제대로 되었는가를 나타낸다(boolean, true or false).
* 닫힌 채널에 값 전송 시 패닉(예외) 발생 


* Range : 채널 안에서 값을 순회하면서 꺼냄. 채널 닫아야(close) 반복문 종료 -> 채널이 열려 있고 값 전송하지 않으면 계속 대기 

* Unbuffered Channel과 Buffered Channel
  * Unbuffered : 이 채널에서는 하나의 수신자가 데이타를 받을 때까지 송신자가 데이타를 보내는 채널에 묶여 있게 된다.
  * Buffered Channel : 비록 수신자가 받을 준비가 되어 있지 않을 지라도 지정된 버퍼만큼 데이타를 보내고 계속 다른 일을 수행할 수 있다

* 버퍼 채널을 이용하지 않는 경우, 아래와 같은 코드는 에러 (fatal error: all goroutines are asleep - deadlock!) 를 발생시킨다. 왜냐하면 메인루틴에서 채널에 1을 보내면서 상대편 수신자를 기다리고 있는데, 이 채널을 받는 수신자 Go루틴이 없기 때문이다.

```go
package main
import "fmt"
 
func main() {
  c := make(chan int)
  c <- 1   //수신루틴이 없으므로 데드락 
  fmt.Println(<-c) //코멘트해도 데드락 (별도의 Go루틴없기 때문)
}
```
* 하지만 아래와 같이 버퍼채널을 사용하면, 수신자가 당장 없더라도 최대버퍼 수까지 데이타를 보낼 수 있으므로, 에러가 발생하지 않는다.
```go
package main
import "fmt"
 
func main() {
    ch := make(chan int, 1)
    //수신자가 없더라도 보낼 수 있다.
    ch <- 101
 
    fmt.Println(<-ch)
}
```

* 채널을 함수의 파라미터도 전달할 때, 일반적으로 송수신을 모두 하는 채널을 전달하지만, 특별히 해당 채널로 송신만 할 것인지 혹은 수신만할 것인지를 지정할 수도 있다. 송신 파라미터는 (p chan<- int)와 같이 chan<- 을 사용하고, 수신 파라미터는 (p <-chan int)와 같이 <-chan 을 사용한다. 만약 송신 채널 파라미터에서 수신을 한다거나, 수신 채널에 송신을 하게되면, 에러가 발생한다.

* 발신 전용 : channel <- 데이터형
* 수신 전용 : 데이터형 <- channel
* 매개 변수를 통해 전용 채널 확인 가능 

```go

func main()  {

	c := make(chan int)

	go sendOnly(c, 10)
	go receiveOnly(c)

	time.Sleep(2 * time.Second)
}

func receiveOnly(c <- chan int) {
	for i := range c{
		fmt.Println("received : ", i)
	}

	fmt.Println(<- c)
}

func sendOnly(c chan <- int, cnt int) {
	for i:= 0; i < cnt; i++{
		c <- i
	}

	c <- 777
}
```

* 채널을 함수의 반환값으로 사용 
```go
package main

import "fmt"

func sum(cnt int) <- chan int {
	sum := 0
	total := make(chan int)
	go func() {
		for i:= 0; i < cnt; i++ {
			sum += i
		}
		total <- sum
	}()
	return total
}

func main() {
	// 채널 함수의 반환 값으로 사용
	c := sum(100)
	fmt.Println(c)
}
```

* 채널 select문
* 채널에 값이 수신되면 해당 case 문 실행
* 일회성 구문이므로 for 안에서 수행
* default 구문 처리 주의 

* 복수 채널들을 기다리면서 준비된 (데이타를 보내온) 채널을 실행하는 기능을 제공한다. 즉, select문은 여러 개의 case문에서 각각 다른 채널을 기다리다가 준비가 된 채널 case를 실행하는 것이다

* select문은 case 채널들이 준비되지 않으면 계속 대기하게 되고, 가장 먼저 도착한 채널의 case를 실행한다. 만약 복수 채널에 신호가 오면, Go 런타임이 랜덤하게 그 중 한 개를 선택한다. 하지만, select문에 default 문이 있으면, case문 채널이 준비되지 않더라도 계속 대기하지 않고 바로 default문을 실행한다.

* 아래 예제는 for 루프 안에 select 문을 쓰면서 두개의 goroutine이 모두 실행되기를 기다리고 있다. 
* 첫번째 run1()이 1초간 실행되고 done1 채널로부터 수신하여 해당 case를 실행하고, 다시 for 루프를 돈다. 
* for루프를 다시 돌면서 다시 select문이 실행되는데, 다음 run2()가 2초후에 실행되고 done2 채널로부터 수신하여 해당 case를 실행하게 된다.

* done2 채널 case문에 break EXIT 이 있는데, 이 문장으로 인해 for 루프를 빠져나와 EXIT 레이블로 이동하게 된다
  * Go에서는 해당 레이블로 이동한 후 자신이 빠져나온 루프 다음 문장을 실행하게 된다
```go

func main() {
    done1 := make(chan bool)
    done2 := make(chan bool)
 
    go run1(done1)
    go run2(done2)
 
EXIT:
    for {
        select {
        case <-done1:
            println("run1 완료")
 
        case <-done2:
            println("run2 완료")
            break EXIT
        }
    }
}
 
func run1(done chan bool) {
    time.Sleep(1 * time.Second)
    done <- true
}
 
func run2(done chan bool) {
    time.Sleep(2 * time.Second)
    done <- true
}
```

# 고루틴 동기화

* 실행 흐름 제어 및 변수 동기화 가능
* `공유 데이터가 보호가 가장 중요`
* 뮤텍스(mutex) : 여러 고루틴에서 작업하는 공유데이터 보호 
* sync.Mutex 선언 후 Lock(), UnLock() 사용

```go

import (
	"sync"
)

func (c *count) increment() {
	c.mutex.Lock()
	c.num +=1
	c.mutex.Unlock()
}
```

* Lock(), UnLock() 으로 공유데이터 보호
* 뮤텍스 : 상호 배제 -> 고루틴(Thread)들이 running time에 서로 영향을 주지 않게 함
* RWMutex : 쓰기 Lock : 쓰기 시도 중에는 다른 곳에서 이전 값을 읽으면 안됌. 읽기 (read), 쓰기(write) 락 전부 방지
* RMutex : 읽기 Lock -> 읽기 시도 중에 값이 변경 방지. 쓰기(write) 락 방지 

```go
mutex := new(sync.RWMutex)

mutex.Lock()

mutex.Unlock()
```

* Wait, Signal(1개만 깨울 때), Broadcas(전체) 함수로 멈춘 쓰레드를 깨움.
```go
var mutex = new(sync.Mutex)
var condition = sync.NewCond(mutex)
condition.Wait()
```












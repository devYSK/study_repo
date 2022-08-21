# 해리 & 션, 범블비, 제리의 MVC 패턴

목차

1. MVC 패턴이란,
2. Model, View, Controller with Code
3. Service
4. References

# MVC 패턴이란

* <img src="https://blog.kakaocdn.net/dn/vPoAs/btrJ7RETeqP/ZZ5gDRBUKW97UhKx7eUes0/img.png">
  * 출처 : 위키피디아 MVC 패턴

* Model & View & Controller로 애플리케이션을 3가지 역할로 구분한 개발 방법론

MVC 패턴이 나오기 이전의 웹 어플리케이션의 아키텍쳐

## 모델 1 아키텍처

![mvc-pattern-1](https://github.com/ksy90101/TIL/blob/master/spring/img/mvc-pattern-1.png?raw=ture)

* [출처](https://rutgo-letsgo.tistory.com/176)

* 구성 : JSP + JavaBean(Service)
    * 한 페이지가 뷰 + 로직으로 이루어져 있다. - 뷰와 로직이 섞인다.
    * 장점 : 개발이 쉽고 구조가 간단하다.
    * 단점 : 코드가 복잡해져서 유지보수가 상당히 까다롭다. 프론트와 백엔드의 분업이 힘들다.

## 모델 2 아키텍쳐 (MVC 패턴과 비슷하다)

![mvc-pattern-2](https://github.com/ksy90101/TIL/blob/master/spring/img/mvc-pattern-2.png?raw=ture)

* [출처](https://rutgo-letsgo.tistory.com/176)

* 구성 : JavaBean(Service) + JSP + 서블릿(컨트롤러)
    * 장점 : 뷰와 로직의 분리로 모델1에 비해 덜 복잡하고, 분업에 더 용이하며, 유지보수가 쉽다
    * 단점 : 모델1에 비해 습득이 어렵고, 작업량이 많다.

## MVC 패턴의 흐름

1. 사용자는 원하는 기능을 처리하기 위한 모든 요청을 컨트롤러에 보낸다.
2. 컨트롤러는 모델을 사용하고, 모델은 알맞은 비즈니스 로직을 수행한다.
3. 컨트롤러는 사용자에게 보여줄 뷰를 선택한다.
4. 선택된 뷰는 사용자에게 알맞는 결과 화면을 보여준다
    * 이 때 사용자에게 보여줄 데이터는 컨트롤러를 통해서 전달받는다.

### Model (모델)

* 값과 기능을 가지고 있는 객체
* 비즈니스 로직을 가지고 있는 객체.
* 도메인 객체나 Entity 같은 객체로, 데이터베이와 연관되어있다.
    * 최근에는 Entity, VO, Aggregate로 나누어 관리한다.- DDD

### Controller(컨트롤러)

* 사용자로부터 요청을 받아 모델 객체를 변경하거나 변경된 모델 객체를 뷰에 전달한다.

* 모델 객체로의 데이터 흐름을 제어
* 뷰와 모델의 역할을 분리한다.
    * 모델은 데이터, 뷰는 화면
    * 그러면서 모델과 뷰를 이어주는 부분이다.

### View(뷰)

* 모델에 포함된 데이터의 시각화
* 사용자 인터페이스를 담당하는 레이어.
* 데이터는 다양하게 표현할 수 있다.
* 화면, HTML, JSP, JSON, XML 등

## MVC 패턴의 장점

* 각 컴포넌트의 코드 결합도를 낮출 수 있다.
    * 모델1 패턴과 달리, 뷰와 모델을 분리함으로써 프론트, 백엔드를 나누어 개발할 수 있다.
* 높은 응집도를 갖는다.
    * 논리적으로 관련이 있는 기능을 하나의 컨트롤러로 묶을 수 있다.


* 코드의 재사용성이 높아진다
* 구현자들 간의 커뮤니케이션 효율성을 높일 수 있다.

`즉 MVC패턴은 이전 방식에 비해 유지보수가 편해지는 코드 방식을 가진 패턴이다`

### 단점

단점이라기보다는 주의사항에 가깝다.

* 잘못된 코드를 짜지 않기 위해 처리 과정을 이해하기 위한 배경지식을 잘 이해해야한다
    * 하지만 러닝커브가 높다
* 모델, 뷰, 컨트롤러의 각 레이어가 각자의 영역, 즉 책임에서 벗어나지 않게 주의 깊게 구성해야 한다.

## MVC 패턴에서 많이 실수하는 부분들

1. Model에서 View의 접근 또는 역할 수행

* 도메인과 뷰의 역할과 책임, 그리고 레이어는 다르다
* 겹치지 않게 별도로 분리해서 사용

2. View에서 일어나는 과한 값 검증과 예외처리
    * 요청에 의한 Input 값 검증을 어느 계층에서 하는게 좋을까?
    * Input 값은 Presentaion Layer에서 체크한다
        * InputView에 값의 입력 외에 다른 역할을 부여하면 [단일 책임 원칙](####단일-책임-원칙-(SRP,-Sigle-Responsibillity-Principle))에 위반되어 추후에 입력
          채널이 달라질 경우 유효성 체크로직도 다른 채널로 옮겨가야 한다.
    * 사용자의 권한 혹은 논리적인 값(존재 유무, 일치 여부) 등은 Service Layer에서 체크하자.
        * 값 형식은 유효하지만, 도메인 모델에서 확인해야할 부분들은 생성자에서 체크
            * (이름은 몇 글자 이상이여야 한다.)
            * 생성자에서는 유효성 체크만 하고 다른 로직은 추가되어서는 안된다
                * 단일책임원칙 위배, DTO를 사용할 경우에는 DTO는 비즈니스 로직이 추가되어선 안된다.

---

#### 단일 책임 원칙 (SRP, Sigle Responsibillity Principle)

* 모든 클래스는 하나의 책임만 가지며, 클래스는 그 책임을 완전히 [캡슐화](https://ko.wikipedia.org/wiki/캡슐화)해야 함을 일컫는다.
* 클래스가 제공하는 모든 기능은 이 책임과 주의 깊게 부합해야 한다.

* 즉 하나의 클래스와 그 클래스로부터 나온 객체는 반드시 하나의 역할과 책임만을 갖는다.

---

3. Controller의 책임은 최대한 가볍게
    1. 로직은 최대한 배제하고, Model과 View의 연결 역할만 하는 책임을 갖도록 구현

## Service Layer의 등장

프로젝트가 커지고 모델이 점점 복잡해짐에 따라 모델 자체에서의 비즈니스 로직만으로는 코드가 너무 복잡해진다.

이것을 해결하기 위해 Service 레이어가 등장하였다.

### Service?

* 비즈니스 로직을 수행하는 메서드를 가지고 있는 객체
    * 여러 도메인들의 복잡한 로직들을 `비즈니스로직을 수행하는 메서드`의 한 트랜잭션 내에서 묶어서 처리
* 비지니스 메서드를 별도의 Service 객체에서 구현하도록 하고, 컨트롤러는 Service 객체를 사용하도록 한다
    * 이렇게 하면 모델이 복잡하고 갯수가 많아져서 여러 모델끼리의 로직이 필요할 때, 묶어서 처리할 수 있게된다.

## MVC를 지키면서 코딩하는 방법

1. Model은 Controller와 View에 의존하지않아야 한다.
    * Model 내부에 Controler와 View에 관련된 코드가 있으면 안 된다.
        * Model 클래스내에 Controller와 View를 사용하는 코드가 있으면 안되는것
    * Model이 Model의 책임 외에 다른 책임을 가져서는 안되고, 오로지 데이터에 대한 순수 로직만 존재해야 한다


2. View는 Model에만 의존해야 하고, Controller에는 의존하면 안 된다.
    * View 내부에 Model의 코드만 있을 수 있고, Controller에 관련된 코드가 있으면 안된다.
        * Model에 관련되어 보여주는 로직만 있어야한다. ex) 모델 클래스의 데이터를 print()
        * 컨트롤러가 Model에 대한 값을 View로 전달 해준다.


3. View가 Model로부터 데이터를 받을 때는 사용자마다 다르게 보여주어야 하는 데이터에 대해서만 받아야 한다.
    * 각 사용자마다 다르게 보여줘야 하는 데이터, 예를들면 자기가 자기자신의 프로필을 조회할 때 등,
        * 철수가 자기 프로필을 조회할 때와 영수가 자기 프로필을 조회할 때의 정보는 다 다르다
    * 모든 사용자에게 공통적으로 보여주는 데이터, 텍스트 등은 모델로부터 받으면 안 된다.
        * 뷰가 자체적으로 가지고 있어야 되는 정보들


4. Controller는 Model과 View에 의존해도 된다.
    * Controller 내부에는 Model과 View의 코드가 있을 수 있다.
    * 컨트롤러는 모델과 뷰의 중개자 역할을 하면서 전체 로직을 구성하기 때문
    * 좋은 패턴은 아니기 때문에 의존 해도 된다지, 반드시 의존하란 뜻은 아니다
        * 결국 컨트롤러는 모델과 계층이 다르기 때문에.


5. View가 Model로부터 데이터를 받을 때 반드시 Controller에서 받아야 한다.
    * 알맞은 요청에 대한 알맞은 서비스를 호출하고 그 결과를 응답으로 돌려줘야 한다.

> 하지만 단순히 MVC패턴만 사용하다가는 현대의 비대한 컨트롤러, 중복 로직, 데이터베이스 처리등과 같은 코드들을
>
> MVC 3레이어만으로는 처리하기 어렵다.
>
> 그래서 현대에 와서는 좀더 세분화된 `5Layer를` 사용한다

## 5Layer

![img](https://blog.kakaocdn.net/dn/b2ZU6Y/btrjWnRhNYa/VpU7YyhEbE3cnOpvyAkuZ1/img.jpg)

* [참조](https://murphymoon.tistory.com/entry/%EC%9A%B0%EC%95%84%ED%95%9C-%ED%85%8C%ED%81%AC-MVC-%EB%A6%AC%EB%B7%B0-%EB%A0%88%EC%9D%B4%EC%96%B4-MVC-%ED%8C%A8%ED%84%B4-5%EB%A0%88%EC%9D%B4%EC%96%B4)

### Presentation Layer

- 사용자에게 보여주는 화면. - UI (User Interface)

### Control Layer

* 컨트롤러

- Presentation Layer와 Business Logic Layer를 연결해주는 역할.
- BusinessLogin Layer를 컨트롤러에서 사용한다.

### Business Logic Layer

* 서비스, 또는 도메인으로 비즈니스 로직을 구성한 레이어.

- 비즈니스 로직을 수행하는 메서드를 가지고 있는 클래스.
- 비즈니스 로직을 별도의 Service 객체에서 구현하도록 하고 컨트롤러가 서비스 객체를 사용하여 요청 처리.
- Service는 Transcation을 가지며 Transaction이 시작하는 시점.
- Contoller와 Persistance Layer의 중간 다리 역할.
- Persistance Layer[데이터베이스]로 값을 저장하거나 가져오는 역할도 수행

### Persistance Layer

* 데이터베이스 레이어. - 영속화

- 데이터를 접근하는 기능을 별도의 Repository나 DAO 객체에서 구현.
- 데이터를 처리하는 부분입니다. (CRUD)
- Service는 Repository 객체나 다른 Service를 사용한다.
- DAO vs Repository
    - [DAO vs Repository Patterns | Baeldung](https://www.baeldung.com/java-dao-vs-repository)
    - [What is the difference between DAO and Repository patterns?](https://stackoverflow.com/questions/8550124/what-is-the-difference-between-dao-and-repository-patterns)

### Domain Model Layer

* 자바 객체인 도메인 모델과 데이터베이스의 테이블을 맵핑하는 역할을 하는 도메인 객체들의 Layer

- 데이터와 행위를 갖는 객체로 핵심 비즈니스 로직이 있으며 주요 검증을 진행
- 식당으로 예를 들자면 요리를 만드는 재료라고 생각하면 좋을거 같습니다. 또한 그 재료를 모은 음식이 될수도 있겠습니다.

## Spring에서의 MVC 흐름

- DispatcherServlet이 Controller에게 Mapping 해주고
- Controller안에 있는 작은 Servlet들이 데이터 조작후 View를 준비하고
- View가 Rendering 해줍니다.

참조

* [제리의 MVC 패턴](https://www.youtube.com/watch?v=ogaXW6KPc8I)
* [범블비의 MVC 패턴](https://www.youtube.com/watch?v=es1ckjHOzTI)
* [해리 & 션의 MVC 패턴](https://www.youtube.com/watch?v=uoVNJkyXX0I&list=PLgXGHBqgT2TvpJ_p9L_yZKPifgdBOzdVH&index=233)
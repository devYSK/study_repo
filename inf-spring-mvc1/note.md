
# 스프링 mvc 1편 - 인프런 김영한님 

전체 목차
1. 웹 애플리케이션 이해
2. 서블릿
3. 서블릿, JSP, MVC 패턴
4. MVC 프레임워크 만들기
5. 스프링 MVC - 구조 이해
6. 스프링 MVC - 기본 기능
7. 스프링 MVC - 웹 페이지 만들기



# Servlet

## 웹 애플리케이션 서버의 요청 응답 구조 

* ![](.note_images/aeb1cb5e.png)

## HttpServletRequest - 개요
**HttpServletRequest** 역할  
HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다. 서블릿은 개발자가
HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다. 그리고 그
결과를 HttpServletRequest 객체에 담아서 제공한다

* START LINE
  * HTTP 메소드
  * URL
  * 쿼리 스트링
  * 스키마, 프로토콜
* 헤더
  * 헤더 조회
* 바디
  * form 파라미터 형식 조회
  * message body 데이터 직접 조회

HttpServletRequest 객체는 추가로 여러가지 부가기능도 함께 제공한다.  
**임시 저장소 기능**
* 해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능
  * 저장: request.setAttribute(name, value)
  * 조회: request.getAttribute(name)
* 세션 관리 기능
* request.getSession(create: true)
> 중요> HttpServletRequest, HttpServletResponse를 사용할 때 가장 중요한 점은 이 객체들이 HTTP 요청
메시지, HTTP 응답 메시지를 편리하게 사용하도록 도와주는 객체라는 점이다. 따라서 이 기능에 대해서
깊이있는 이해를 하려면 HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 한다.


## HTTP 요청 데이터 - 개요
* 주로 다음 3가지 방법을 사용한다.
* GET - 쿼리 파라미터
  * /url?username=hello&age=20
  * 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
  * 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
* POST - HTML Form
  * content-type: application/x-www-form-urlencoded
  * 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
  * 예) 회원 가입, 상품 주문, HTML Form 사용
* HTTP message body에 데이터를 직접 담아서 요청
  * HTTP API에서 주로 사용, JSON, XML, TEXT
* 데이터 형식은 주로 JSON 사용
  * POST, PUT, PATCH

## HTTP 요청 데이터 - GET 쿼리 파라미터
* 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
* 쿼리 파라미터는 URL에 다음과 같이 ? 를 시작으로 보낼 수 있다. 추가 파라미터는 & 로 구분하면 된다.
> http://localhost:8080/request-param?username=hello&age=20
* 서버에서는 HttpServletRequest 가 제공하는 다음 메서드를 통해 쿼리 파라미터를 편리하게 조회할 수
있다.
### 쿼리 파라미터 조회 메서드
```java
String username = request.getParameter("username"); //단일 파라미터 조회
Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들 모두 조회
Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map 으로 조회
String[] usernames = request.getParameterValues("username"); //복수 파라미터 조회
```

## HTTP 요청 데이터 - POST HTML Form
HTML의 Form을 사용해서 클라이언트에서 서버로 데이터를 전송.
* 주로 회원 가입, 상품 주문 등에서 사용하는 방식이다.
* 특징
* content-type: application/x-www-form-urlencoded
  * 메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. username=hello&age=20

* application/x-www-form-urlencoded 형식은 앞서 GET에서 살펴본 쿼리 파라미터 형식과 같다.
* 따라서 쿼리 파라미터 조회 메서드를 그대로 사용하면 된다. -> request.getParameter();
* 클라이언트(웹 브라우저) 입장에서는 두 방식에 차이가 있지만, 서버 입장에서는 둘의 형식이 동일하므로,
request.getParameter() 로 편리하게 구분없이 조회할 수 있다

### 참고
> content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.  
> ₩GET URL 쿼리 파라미터 형₩식으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를
사용하지 않기 때문에 content-type이 없다.  
> `POST HTML Form 형식`으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기
때문에 바디에 포함된 데이터가 어떤 형식인지 content-type을 꼭 지정해야 한다. 이렇게 폼으로 데이터를
전송하는 형식을 `application/x-www-form-urlencoded` 라 한다.

## HttpServletResponse - 기본 사용법
### HttpServletResponse 역할
* HTTP 응답 메시지 생성
  * HTTP 응답코드 지정
  * 헤더 생성
  * 바디 생성
* 편의 기능 제공
  * Content-Type, 쿠키, Redirect


>  application/json 은 스펙상 utf-8 형식을 사용하도록 정의되어 있다. 그래서 스펙에서 charset=utf-8
과 같은 추가 파라미터를 지원하지 않는다. 따라서 application/json 이라고만 사용해야지
> application/json;charset=utf-8 이라고 전달하는 것은 의미 없는 파라미터를 추가한 것이 된다.
> response.getWriter()를 사용하면 추가 파라미터를 자동으로 추가해버린다. 이때는
response.getOutputStream()으로 출력하면 그런 문제가 없다.


## JSP 라이브러리 추가
```
//JSP 추가 시작
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'javax.servlet:jstl'
//JSP 추가 끝
```

* JSP는 자바 코드를 그대로 다 사용할 수 있다.
* <%@ page import="hello.servlet.domain.member.MemberRepository" %>
  * 자바의 import 문과 같다.
* <% ~~ %>
  * 이 부분에는 자바 코드를 입력할 수 있다.
* <%= ~~ %>
  * 이 부분에는 자바 코드를 출력할 수 있다.

## MVC 패턴 - 개요

### 너무 많은 역할
* 하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 모두 처리하게 되면, 너무 많은 역할을
하게되고, 결과적으로 유지보수가 어려워진다. 
* 비즈니스 로직을 호출하는 부분에 변경이 발생해도 해당 코드를 손대야 하고, UI를 변경할 일이 있어도 비즈니스 로직이 함께 있는 해당 파일을 수정해야 한다


### 변경의 라이프 사이클 

* 진짜 문제는 둘 사이에 변경의 라이프 사이클이 다르다는 점이다. 예를 들어서 UI
  를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분
  서로에게 영향을 주지 않는다. 
* 이렇게 변경의 라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은 유지보수하기 좋지 않다


진짜 문제는 둘 사이에 변경의 라이프 사이클이 다르다는 점이다. 예를 들어서 UI
를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분
서로에게 영향을 주지 않는다. 이렇게 변경의 라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은
유지보수하기 좋지 않다

## Model View Controller
MVC 패턴은 지금까지 학습한 것 처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와
뷰(View)라는 영역으로 서로 역할을 나눈 것을 말한다. 웹 애플리케이션은 보통 이 MVC 패턴을 사용한다.
### 컨트롤러
* HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과
* 데이터를 조회해서 모델에 담는다.
### 모델 
* 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는
비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
### 뷰
* 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다.  HTML을 생성하는 부분을 말한다.
> 참고
> 컨트롤러에 비즈니스 로직을 둘 수도 있지만, 이렇게 되면 컨트롤러가 너무 많은 역할을 담당한다. 그래서
일반적으로 비즈니스 로직은 서비스(Service)라는 계층을 별도로 만들어서 처리한다. 그리고 컨트롤러는
비즈니스 로직이 있는 서비스를 호출하는 역할을 담당한다. 참고로 비즈니스 로직을 변경하면 비즈니스
로직을 호출하는 컨트롤러의 코드도 변경될 수 있다. 앞에서는 이해를 돕기 위해 비즈니스 로직을
호출한다는 표현 보다는, 비즈니스 로직이라 설명했다.

# 프론트 컨트롤러 

* ![](.note_images/e192933e.png)

## FrontController 패턴 특징
* 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
* 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
* 입구를 하나로!
* 공통 처리 가능
* 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨
## 스프링 웹 MVC와 프론트 컨트롤러
### 스프링 웹 MVC의 핵심도 바로 FrontController
### 스프링 웹 MVC의 DispatcherServlet이 FrontController 패턴으로 구현되어 있음


# 스프링 MVC - 구조 이해









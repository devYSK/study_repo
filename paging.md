## 컨트롤러에 전송할 파라미터 객체

```java
package com.example.demo.dto;

import lombok.Getter;

@Getter
public class PageParam {

	// 인스턴스생성시 원하는 기본값 설정
	private int page = 1; // offset
	private int pageSize = 10; // limit
	
  //Mybatis Mapper에 바인딩 시 활용됨
	private int start = 0;
	
	public PageParam() {
	}
	
	public void pageSize(int pageSize) {
		this.pageSize = pageSize;
		this.start = (page - 1 ) * pageSize; // offset만큼 읽어온다 
	}
	
	public void setPage(int page) {
		this.page = page;
		this.start = (page - 1) * pageSize;
	}
}
```

기본적으로 page, pageSize 를 전달받는 역할을 한다
page와 pageSize 를 선택적으로 전송 할 수 있도록 객체 초기화시 초기값을
설정 해둔다.

## 페이지 화면에 전달할 Paging Model

```java
package com.example.demo.dto;

import lombok.Getter;

@Getter
public class PageDTO {

	private int endPage;
	private int startPage;
	private int realEnd;
	private int total;
	
	boolean prev, next;
	
  //앞서 작성한 PageParam 객체를 필드로 사용한다
	private PageParam pageParam;

	//pageParam을 파라미터로 받아와 페이지 연산에 활용한다
	public PageDTO(PageParam pageParam, int total) {
		
		this.pageParam = pageParam;
		this.total = total;
		
		int current = pageParam.getPage();
		int amount = pageParam.getAmount();
		
    // 페이징의 끝번호 구하기!
    // Math.ceil은 소숫점 자리에서 올림을 한다
		this.endPage = (int)( Math.ceil(current*0.1))*10;
        
    // 페이징의 시작번호 구하기!
    // (현재 보이는 페이지의 끝번호) - (한 화면에 보여질 페이지 개수 - 1) 
		this.startPage = endPage - 9; 
		
		this.realEnd = (int)Math.ceil(total/amount);
		
        	// 실제 끝번호 보다 endPage가 큰경우 실제 번호로 대입한다
		if(realEnd < endPage) {
			this.endPage = realEnd;
		}
		
		this.prev = current > 1;
		this.next = current < realEnd;
	}
}
```

이 객체가 실제 페이지 화면으로 전달될 페이지 **Model**이다
우리에게 주어진 속성은 현재페이지, 목록갯수, 총목록갯수 이렇게
세가지다. 이걸 기준으로 endPage, startPage, (prev, next)-이전,다음 버튼 표시 여부
를 계산해야 한다. 계산을 하는데 있어서 가장 핵심적으로 알아야 할 부분은
**끝번호를 먼저 계산 하는것** 이것이 가장 중요한 부분이다

> 시작번호는 끝번호에 (표시되는 페이지의 갯수 -1) 만큼 빼주기만 하면 계산이 끝난다

이러한 구성의 코드를 처음 본다면 의아한 부분이 존재한다
endPage?? realEnd?? 왜 끝번호가 두개나 있는건지 의아할 것이다

만약 **15페이지 구성에 현재페이지가 11페이지 라면 endPage=20, realEnd=15**가 될것이다
위 예시에 상황이라면 페이지가 **20페이지 까지 표시**가 되어버릴것이다 그렇기 때문에 실제 끝번호
보다 endPage가 큰 경우라면 **endPage에 realEnd를 대입시켜 예외처리**를 한다



## SQL 처리

```sql
SELECT * 
FROM board b 
LEFT JOIN member m
ON b.writer_id = m.member_id 
ORDER BY b.board_no DESC
LIMIT #{start}, #{pageSize}
```

목록을 전부 조회할때와 동일하지만 조회 해야할 시작번호와 조회할 목록 크기를 바인딩하여
LIMIT을 걸어 컬럼 수를 제한한다 **MySQL을 사용중이기 때문에 LIMIT을 사용**했지만
오라클을 포함한 다른 RDB들은 **제각각 페이징을 처리하는 방법이 다르다** ORM을 사용하지 않는다면 직접 검색을 통해 가장 성능이 좋은 방법을 찾아내자

## Controller

```java
	@GetMapping("/list")
	public void list(PageParam page, Model model) {
		
		int total =  boardService.getTotalCount(page);
		PageDTO pageDTO = new PageDTO(page, total);
		
		model.addAttribute("boardList", boardService.getListPaging(page));
		model.addAttribute("page", pageDTO);
	}
```

컨트롤러 에서는 전달받은 page 요청 파라미터와, 미리 구현해둔 목록의 총 개수를 구해 PageDTO 객체를 생성하고, 페이징 처리된 목록리스트를 받아와 Model에 담아 뷰로 전달한다.
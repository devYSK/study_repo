

# Git Commit Message



* [커밋 메시지란?](##**커밋-메시지란?**)
* [커밋 메시지를 잘 쓰려고 노력해야 하는 이유](##git-커밋 메시지를-잘-쓰려고-노력해야-하는-이유)
* [커밋 메시지 옵션](##커밋-메시지-옵션)
* [좋은 git 커밋 메시지를 작성하기 위한 7가지 약속](##좋은-git-커밋-메시지를-작성하기-위한-7가지 약속)

* [커밋 메시지 작성 구조(문법)](##커밋-메시지-작성-구조(문법))



---

### **커밋 메시지란?**

* 작업중인 로컬 디렉터리에서 git add를 하게되면 변경된 파일의 목록이 stage에 추가가 된다.

* 변경된 파일의 목록들을 HEAD(확정본)에 반영을 시킬 때 git commit을 쓰게 된다

* commit message는 쉽게 말하면 HEAD에 어떤 변화가 반영이 되었는지 설명하기 위한 글이다



# git 커밋 메시지를 잘 쓰려고 노력해야 하는 이유

1. 더 좋은 커밋 로그 가독성
2. 더 나은 협업과 리뷰 프로세스
3. 더 쉬운 코드 유지보수

이 내용이 강제사항은 아니며 모든 커밋 메시지를 제목과 본문으로 구성할 필요는 없습니다. 

때에 따라서는 아래의 예와 같이 변경사항을 한 줄로 요약한 커밋 메시지가 더 효율적입니다.

```
아 직전 커밋 중 `README.md`에 들어간 오타 한글자 고침 아 (부끄)
```

```
쌍따옴표 한 개 빼먹었다..--; 수정
```

```
어제부로 저장소 URL이 바뀜. URL 한 개만 업데이트
```



## 커밋 메시지 옵션

### **Commit Options**

- -m

**커밋 메시지를 작성**

```
git add file
git commit -m "fix 어떠한 기능 수정"
```

 

- -a or --all

**모든 파일을 자동으로 Commit(될 수 있으면 쓰지 않는 것을 추천)**

```
git commit -a -m "feat 어쩌구저쩌구 기능 추가"
```

 

- --amend

**원격 저장소로 푸쉬되지 않은 마지막 커밋 메시지를 다시 작성**

```
git add .

git commit --amend -m "ADD 다른 메시지 작성"
```



# 좋은 git 커밋 메시지를 작성하기 위한 7가지 약속

> 이하 약속은 커밋 메시지를 `English`로 작성하는 경우에 최적화되어 있습니다. 
> 한글 커밋 메시지를 작성하는 경우에는 더 유연하게 적용하셔도 좋을 것 같네요.

1. 제목과 본문을 한 줄 띄워 분리하기
2. 제목은 영문 기준 50자 이내로
3. 제목 첫글자를 대문자로
4. 제목 끝에 `.` 금지
5. 제목은 `명령조`로
6. 본문은 영문 기준 72자마다 줄 바꾸기
7. 본문은 `어떻게`보다 `무엇을`, `왜`에 맞춰 작성하기



### 1. 제목과 본문을 한 줄 띄워 분리하기

```she
feat : 새로운 기능 추가.
// 공백 한줄
본문............
.............
```



다음과 같이 제안을 따른 커밋 메시지가 있습니다. 제목, 공백 한 줄, 본문으로 구성되어 있습니다.

```
Derezz the master control program

MCP turned out to be evil and had become intent on world domination.
This commit throws Tron's disc into MCP (causing its deresolution)
and turns it back into a chess game.
```

`git log`는 이 커밋 메시지를 다음과 같이 보여줍니다.

```
$ git log
commit 42e769bdf4894310333942ffc5a15151222a87be
Author: Kevin Flynn <kevin@flynnsarcade.com>
Date:   Fri Jan 01 00:00:00 1982 -0200

 Derezz the master control program

 MCP turned out to be evil and had become intent on world domination.
 This commit throws Tron's disc into MCP (causing its deresolution)
 and turns it back into a chess game.
```

별다를게 없네요. 여기서 `git log --oneline` 옵션을 사용해 봅니다.

```
$ git log --oneline
42e769 Derezz the master control program
```

짠! 제목만 보여줍니다. 한 줄 공백으로 분리하지 않았다면 어떻게 보였을까요?

```
$ git log --oneline
42e769 Derezz the master control program MCP turned out to be evil and had become intent on world domination. This commit t
```

 

## 2. 제목은 영문 기준 50자 이내로

*  강제로 제한하는 것은 아니고 읽기 쉽고 간결하게 표현하기 위한 경험에 의한 규칙이다

* '50자 이내'라는 규칙은 지키기 그리 어려운 제약사항이 아닙니다.
*  이 작은 강제로 커밋 메시지 작성자는 더 읽기 좋은 커밋 제목을 만들 수 있고, 가장 간결히 요약된 제목을 작성할 수 있습니다. 
* 또는 그렇게 만들 방법을 고민하는 습관을 들이는데 도움을 주죠.

## 3. 제목 첫글자를 대문자로

* 거의 영문법의 문제. 첫글자는 대문자야 하는것
* ex)
  * Fix trigger error -> **O**
  * fix trigger error  -> **X**

## 4. 제목 끝에 `.` 금지

* 이 역시 영문법의 문제. 제목에는 보통 점을 찍지 않는다고 한다.

* 제목 행의 끝에는 마침표가 필요 없다.  50자 규칙에 따르기 위해서라도 마침표를 넣는 것은 불필요한 공간 낭비이다

  * Open the pod bay **doors.**  -> **X**

  - Open the pod bay **doors**   -> **O**



## 5. 제목은 `명령조`로

* 이는 첫 단어를 `동사원형`으로 쓰라는 의미.
* 명령이나 설명하듯이 작성한다.
* 커밋 메시지는 과거의 기록
* git 스스로가 자동 커밋을 작성할 때도 명령문을 사용 
* 커밋 제목을 `명령문`으로 작성하면, 자동 메시지로 채워진 커밋 사이에 자연스레 녹아든다
* 이 역시 반드시 명령조로 시작할 필요는 없다. 자연스럽게 과거형이나 현재형 시제를 사용해도 된다. 



## 6. 본문은 영문 기준 72자마다 줄 바꾸기

* git은 자동으로 커밋 메시지 줄바꿈을 하지 않는다.
* git log 명령어로 메시지를 보면 줄바꿈이 되어 나오지 않는다. 
* 단순한 `git log` 명령어 입력만으로도 보기 좋은 메시지를 만들수 있다.
* 그 적당한 위치로 `72자`마다 줄바꿈을 하는것. 

## 7. 본문은 `어떻게`보다는 =>  `무엇을`, `왜`에 맞춰 작성하기

* 변경한 내용과 이유를 자세하게 설명. 
* 짧은 헤더로 충분히 표현 못할 이유이므로 이해할 수 있게 작성하는것이 중요하다.  
  * 리뷰어가 원래 문제가 무엇인지 이해한다고 가정하지 말고 확실하게 설명을 적어주자
  * 내 코드는 직관적이지 않다. 

## !팀의 커밋 규칙을 잘 따르자!



---

<br>



## 커밋 메시지 작성법 및 유형

 ```shell
 feat(feature) : 새로운 기능에 추가에 대한 커밋
 
 fix(bug fix) : 버그 수정 관련 커밋
 
 refactor : 코드 리팩토링에 대한 커밋
 
 test : 테스트 코드 수정에 대한 커밋
 
 comment : 필요한 주석 추가 및 변경
 
 build : 빌드 관련 파일 수정에 대한 커밋
 
 chore : 그 외 자잘한 수정에 대한 커밋(기타 변경). 빌드 업무 수정, 패키지 매니저 수정(ex .gitignore 수정 같은 경우)
 
 ci : CI 관련 설정 수정에 대한 커밋
 
 docs(documentation) : 문서 추가, 수정, 삭제에 대한 커밋
 
 style : 코드 스타일 혹은 포맷 등에 관한 커밋. 세미콜론(;) 누락, 코드 변경이 없는 경우
 
 design : CSS 등 사용자 UI 디자인 변
 
 rename : 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우
 
 remove : 파일을 삭제하는 작업만 수행한 경우
 
 hotfix : 급하게 치명적인 버그를 고쳐야 하는 경우
 ```





## 커밋 메시지 작성 구조(문법)

헤더는 필수이며, 범위(scope), 본문(body), 바닥글(footer)은 선택사항입니다.

```sh
<type> (<scope>): <subject>    -- 헤더    => 제목(간단 요약)
<BLANK LINE>                  -- 빈 줄   => 빈칸
<body>                        -- 본문    => 내용
<BLANK LINE>                  -- 빈 줄   => 빈칸
<footer>                      -- 바닥 글  => 주요 변경 사항, 이슈 해결 사항
```

`<type>`은 해당 **commit의 성격**을 나타냅니다. - [커밋메시지유형](##커밋-메시지-유형) 



### 1. 제목 (subject)

- `<type>` : 변경 사항의 특징에 따라 적는다.

- `<scope>` : 변경이 된 위치를 적는다.

  - 변경된 클래스, 메소드의 이름을 적는다.

- `<subject>` : 변경 내용을 간단하게 한줄로 요약해서 적는다.

  - 첫 글자를 소문자로 쓴다
  - 마지막에 `.` 을 쓰지 않는다.
  - 명령형으로 쓰고, 현재형으로 적는다. (change(O) → changed(X), changes(X))

  

#### 1.1 제목(Subject) 

- 제목은 50자를 넘기지 않고, 마침표를 붙이지 않는다.
  - 제목에는 commit <type>을 함께 작성한다.
- 과거 시제를 사용하지 않고 명령조로 작성한다.
- 제목과 본문은 한 줄 띄워 분리합니다. -> 빈 줄
- 제목의 첫 글자는 반드시 대문자로 쓴다.
- 제목이나 본문에 이슈 번호(가 있다면) 붙여야 한다.



##### 타입(Type) - 제목(Subject) 예시[Permalink](https://junhyunny.github.io/information/github/git-commit-message-rule/#타입type---제목subject-예시)

```
Feat: 신규 RFID 인식 기능 추가
```

### 2. 본문(Body)

- 선택 사항이기에 모든 commit에 본문 내용을 작성할 필요는 없다. -> 간단한 내용이면 제목만 적어도 된다. 
- 영문 기준으로 한 줄에 72자를 넘기면 안 된다.
- 어떻게(How)보다 무엇을, 왜(What, Why)에 맞춰 작성.
- 설명뿐만 아니라, commit의 이유를 작성할 때에도 쓴다.

```
신규 RFID 기능 인식 기능 추가
  - RFIDReader.java: 사용자 요건 사항으로 인한 RFID 인식 기능 추가
```

### 3. 꼬리말(Footer)

- 선택 사항이므로 모든 commit에 꼬리말을 작성할 필요는 없다.
- 주로 Closes(종료), Fixes(수정), Resolves(해결), Ref(참고), Related to(관련) 키워드를 사용
- Issue tracker ID를 작성할 때 사용합니다.
- 해결: 이슈 해결 시 사용
- 관련: 해당 commit에 관련된 이슈 번호
- 참고: 참고할 이슈가 있는 경우 사용

```
해결: #123
관련: #321
참고: #222
```

##### 작성할 Commit 메세지 예시

```
Feat: 신규 RFID 인식 기능 추가(#123)

신규 RFID 기능 인식 기능 추가
  - RFIDReader.java: 사용자 요건 사항으로 인한 RFID 인식 기능 추가

해결: #123
```







* [Tip, Commit 메세지로 이슈 자동 종료시키는 방법](https://junhyunny.github.io/information/github/git-commit-message-rule/#3-tip-commit-메세지로-이슈-자동-종료시키는-방법)

* [커밋메시지 템플릿](https://jeong-pro.tistory.com/207)

* [이모지를 사용하기](https://gitmoji.dev/)



## 참조

* https://meetup.toast.com/posts/106
* https://richone.tistory.com/26
* https://junhyunny.github.io/information/github/git-commit-message-rule/

* https://nohack.tistory.com/17
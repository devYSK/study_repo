# Asciidoc이란



asciidoc은 다음과 같이 정의 된다.

- 노트, 기사, 문서, 서적, 웹페이지, 블로그게시물 등의 페이지를 작성하기 위한 완성된 텍스트 작성 형식(규격)
- HTML,PDF 등을 포함한 다양한 형식으로 AsciiDoc 문서를 번역하기 위한 텍스트 프로세서

 

`AsciiDoc`은 `경량 마크업 언어`에 속한다. 경량 마크업언어의 대표적인 예로 `mark down`이 존재한다 

 

즉, 간단한 기호와 태그 등으로 문서 편집을 쉽고 빠르고 아름답게(?) 만들도록 도와주는 경량 마크업 언어이다.



> AsciiDoc은 메모, 문서, 기사, 서적, 전자책, 슬라이드쇼, 웹 페이지, 매뉴얼 페이지 및 블로그 작성을 위한 텍스트 문서 형식입니다. AsciiDoc 파일은 Asciidoctor 도구 체인을 통해 HTML, PDF, EPUB, DocBook 및 매뉴얼 페이지를 포함한 다양한 형식으로 변환할 수 있습니다. 모든 텍스트 편집기를 사용할 수 있습니다. Github와 같은 일부 웹사이트는 AsciiDoc 파일을 HTML로 직접 렌더링합니다.
>
> * https://www.vogella.com/tutorials/AsciiDoc/article.html



* 다음은 John Gruber가 Markdown을 소개한 방법이다.(2004년 3월).

> Markdown 서식 구문의 최우선 설계 목표는 가능한 한 읽기 쉽게 만드는 것입니다.
>
> Markdown 형식의 문서는 태그나 형식 지정 지침으로 마크업된 것처럼 보이지 않고 있는 그대로 일반 텍스트로 게시할 수 있어야 합니다.
>
> Markdown 구문에 대한 가장 큰 영감의 원천은 일반 텍스트 이메일 형식입니다.
>
> — Markdown 창시자 John Gruber



* 마찬가지로 Stuart Rackham이 AsciiDoc(2년 전)을 도입하면서 소개한 방법은 다음과 같다.

> 일반 텍스트 문서를 작성하는 것과 같은 방식으로 AsciiDoc 문서를 작성합니다. 마크업 태그나 이상한 형식 표기가 없습니다. AsciiDoc 파일은 직접 보고, 편집하고, 인쇄하거나 다른 프레젠테이션 형식으로 변환하도록 설계되었습니다.
>
> — AsciiDoc의 창시자 Stuart Rackham
>
> *이러한 언어는 사람이 문서를 작성하고 다른 사람이 원시* 형식 으로 ***그대로\*** 읽을 수 있도록 설계되었습니다 .



### 간단한 Asciidoc 파일의 예제

다음 목록은 간단한 Asciidoc 파일의 예입니다.

```ascii
= The First Asciidoc example: Subtitle			// 1       
Lars Vogel(c) 2016 vogella GmbH         		// 2                        
Version 0.3, 17.05.2021                 		// 3                        
:sectnums:                             		 	// 4                        
:toc:                                   		// 5                        
:toclevels: 4                               // 6
:toc-title: My Content                      // 7                        
                                            // 8                    
:description: Example AsciiDoc document     // 9                        
:keywords: AsciiDoc                         // 10                       
:imagesdir: ./img                           // 11                

This is the optional preamble (an untitled section body). Useful for
writing simple sectionless documents consisting only of a preamble.

== First section

Some text

== Second section

More test
```



1. 이것은 자막을 포함한 문서 제목입니다.

2. 제목 다음 줄에 저자를 나열할 수 있습니다.

3. 이것은 문서의 개정판입니다
4. 이 속성은 번호가 매겨진 섹션을 원함을 정의합니다.

5. 목차가 생성됩니다

6. 기본적으로 목차는 수준 1 및 수준 2 섹션 제목만 표시합니다. toclevels 속성을 사용하면 레벨을 다르게 설정할 수 있습니다.

7.  기본적으로 목차는 "목차"라는 제목을 갖게 되며 toc-title 속성을 사용하여 사용자 지정 이름을 설정할 수 있습니다. 이는 html 및 pdf 출력에 적용됩니다

8.  버튼 및 메뉴 경로와 같은 실험적 기능 사용

9. HTML 출력에 설명 추가

10. HTML 출력에 키워드를 추가합니다.

11. 기본 이미지 위치를 정의합니다 Asciidoc 문서는 일반적으로 *.adoc* 로 끝납니다.



## AsciiDoc 문법

* https://docs.asciidoctor.org/asciidoc/latest/syntax-quick-reference/





### 문단 (Paragraphs)

문단은 빈줄로 구분된다. 빈줄이 없이 쓰여진 문장은 하나의 문단으로 인식 되어 이어서 작성된다.



* 다음과 같이 작성된 문장은

```
This journey begins one late Monday afternoon in Antwerp.
Our team desperately needs coffee, but none of us dare open the office door.

To leave means code dismemberment and certain death.
```

* 다음처럼 이어진 문장으로 만들어진다.



```
This journey begins one late Monday afternoon in Antwerp.Our team desperately needs coffee, but none of us dare open the office door.

To leave means code dismemberment and certain death.
```



### 줄바꿈(line break)

문단 안에서 줄 바꿈을 쓰고 싶은 경우 +를 붙이거나 hardbreaks 속성을 주면 된다.

### + 

```
Rubies are red, +
Topazes are blue.
```

  


```adoc
Rubies are red, +
Topazes are blue.
```



### :hardbreaks

```
[%hardbreaks]
Ruby is red.
Java is black.
```

  


```
// 공백
Ruby is red.
Java is black.
```

문서 전체에서 소스의 줄바꿈을 출력의 줄바꿈으로 사용하고 싶다면 문서에 `hardbreaks` 속성을 부여하면 된다.

```
= Line Break Doc Title
:hardbreaks:

Rubies are red,
Topazes are blue.
```

### 경고(Admonitions)

사용자의 주의를 이끌기 위해 경고를 쓰고 싶다면 다음 5가지 중 하나를 사용하면 된다. 

형식은 <경고 레빌>: <내용> 이다.

- NOTE
- TIP
- IMPORTANT
- CAUTION
- WARNING

경고를 주고 싶을때 새로운 문단을 시작하고 대문자로 경고 라벨을 기술한후에 `:`를 붙이고 한칸 띄고 내용을 기술하면 됩니다.

```
NOTE: 참고하고 하세요

TIP: 팁입니다

IMPORTANT: 중요합니다.

CAUTION: 주의 하세요

WARNING: 위험합니다.
```

 


```markdown
NOTE: 참고하고 하세요
TIP: 팁입니다
IMPORTANT: 중요합니다.
CAUTION: 주의 하세요
WARNING: 위험합니다.
```



* 문서에 `icons` 속성을 설정을 이용해 아이콘을 사용할수도 있다.



### Text formating (텍스트 포맷, 문장 강조 )

문장을 강조하기 위해 Quoted text를 사용할 수 있다.  Quoted text는 어떤 기호로 둘러싸여서 특별한 의미를 전달하는 문장을 의미한다.

  


```
*bold phrase* & **char**acter**s**

_italic phrase_ & __char__acter__s__

*_bold italic phrase_* & **__char__**acter**__s__**

`monospace phrase` & ``char``acter``s``

`*monospace bold phrase*` & ``**char**``acter``**s**``

`_monospace italic phrase_` & ``__char__``acter``__s__``

`*_monospace bold italic phrase_*` &
``**__char__**``acter``**__s__**``
```

  


```markdown
*bold phrase* & **char**acter**s**

_italic phrase_ & __char__acter__s__

*_bold italic phrase_* & **__char__**acter**__s__**

`monospace phrase` & ``char``acter``s``

`*monospace bold phrase*` & ``**char**``acter``**s**``

`_monospace italic phrase_` & ``__char__``acter``__s__``

`*_monospace bold italic phrase_*` &
``**__char__**``acter``**__s__**``
```



* Quoted Text는 prefix형태로 속성을 부여할수 있다. 
* 이를 통해 문서 변환 과정에서 해당 quoted text에 추가적인 스타일을 부여할수 있습니다.



예를 들어 다음과 같이 속성을 부여하면

**소스**

```
Type the word [.userinput]#asciidoc# into the search bar.
```

이렇게 별도의 속성을 주면 HTML로 변환할때 해당 부분을 `<span>`으로 감싸고 CSS Class로 부여한다. .



**HTML**

```
<span class="userinput">asciidoc</span>
```

이런 방법을 이용해 자유롭게 스타일을 추가 할 수 있다..

### 치환 방지



만약 문장자체가 quoted text에서 사용하는 기호들을 사용하는 것이라면 어떻게 해야 할까??

이스케이프 문자로서 `\` 를 해당 기호 앞에 기술해 주면 됩니다. 만약 기호가 `__` 또는 `**`처럼 두글자로 된 기호 라면 이스케이프 문자도 두번 기술해주어야 합니다.

* 문장 자체에서 quoted text에 사용한 기호들을 사용하고 싶은 경우 문자 앞에 \를 붙여주면 된다. \*, \_ 와 같이 말이다.

```
\*Stars* will appear as *Stars*, not as bold text.

\&sect; will appear as an entity, not the &sect; symbol.

\\__func__ will appear as __func__, not as emphasized text.

\{two-semicolons} will appear {two-semicolons}, not resolved as ;;.
```



```
\*Stars* will appear as *Stars*, not as bold text.

\&sect; will appear as an entity, not the &sect; symbol.

\\__func__ will appear as __func__, not as emphasized text.

\{two-semicolons} will appear {two-semicolons}, not resolved as ;;.
```





### 목록(List)

목록을 표시하고 싶다면 * 또는 -로 시작하는 문장을 쓰면 된다.  
 목록에 제목을 넣고 싶다면 .을 붙이면 되고 이때 .과 제목 사이에 공백은 없어야한다.

* 목록에 제목을 기입하고 싶다면 `.`으로 시작하는 제목을 기입한후에 목록을 작성하면 된다. 
* 이때 공백이 없어야 한다. 있다면 추후 순서형 목록과 혼돈이 발생할수 있다다.

```
.Kizmet's Favorite Authors
* Edgar Allen Poe
* Sheri S. Tepper
* Bill Bryson
```

   


```markdown
.Kizmet's Favorite Authors
* Edgar Allen Poe
* Sheri S. Tepper
* Bill Bryson
```



서로 다른 목록을 구분 하기 위해서는 주석을 가지는 빈 줄을 넣으면 된다 .

```
* Apples
* Oranges

//-

* Walnuts
* Almonds
```

 


```markdown
* Apples
* Oranges

//-

* Walnuts
* Almonds
```



### 중첩된 목록(List)

중첩된 목록을 가지고 싶다면 중첩만큼 `*`을 입력하면 된다. 최대 5단계까지 중첩 가능합니다.



```
.Possible DefOps manual locations
* West wood maze
** Maze heart
*** Reflection pool
** Secret exit
* Untracked file in git repository
```

 


```markdown
.Possible DefOps manual locations
* West wood maze
** Maze heart
*** Reflection pool
** Secret exit
* Untracked file in git repository
```



#### 순서를 가지는 목록

순서를 가지는 목록을 만들고 싶다면 숫자를 붙이면 된다.

*  <숫자>. 를 이용하면 된다. 숫자를 자동으로 넣고 싶다면  . 으로 해도 된다.

소스

```
1. Protons
2. Electrons
3. Neutrons
```

출력

```markdown
1. Protons
2. Electrons
3. Neutrons
```



하지만 숫자가 아직 결정 안되었거나, 자동으로 넣고 싶다면 `.`으로 시작하면 된다.

소스

```
. Protons
. Electrons
. Neutrons
```

출력

```markdown
. Protons
. Electrons
. Neutrons
```



만약 정렬된 목록을 만들기 위해 숫자를 사용하기로 하였다면, 숫자는 반드시 일련성을 가져야 한다.

*  또한 새로운 번호에서 시작하고 싶다면 그 숫자로 시작하면 된다.

소스

```
4. Step four
5. Step five
6. Step six
```

출력

1. Step four
2. Step five
3. Step six

`.`으로 시작하는 목록에서 새로운 번호에서 시작하고 싶다면 start 속성을 주면 됩니다. 

* 숫자는 항상 1부터 시작함으로 원하는 숫자부터 주고 싶다면 start 속성을 정해주면 된다.

소스

```
[start=4]
 . Step four
 . Step five
 . Step six
```

출력

```markdown
[start=4]
 . Step four
 . Step five
 . Step six
```



[%reversed]를 이용하면 역순도 가능하다. 



소스

```
[%reversed]
.Parts of an atom
. Protons
. Electrons
. Neutrons
```

출력

```markdown
[%reversed]
.Parts of an atom
. Protons
. Electrons
. Neutrons
```



중첩도 가능하다 



```
. Step 1
. Step 2
.. Step 2a
.. Step 2b
. Step 3
```

출력

```markdown
. Step 1
. Step 2
.. Step 2a
.. Step 2b
. Step 3
```



넘버링 스타일은 단계별로 다르게 적용된다.

| Level | Numbering Scheme | Exaples     | CSS Class(HTML converter) |
| ----- | ---------------- | ----------- | ------------------------- |
| 1     | Arabic           | 1. 2. 3.    | arabic                    |
| 2     | Lower Alpha      | a. b. c.    | loweralpha                |
| 3     | Lower Roman      | i. ii. iii. | lowerroman                |
| 4     | Upper Alpha      | A. B. C.    | upperalpha                |
| 5     | Upper Roman      | I. II. III. | upperroman                |

넘버링 스타일도 변경할수 있다..

소스

```
[lowerroman, start=5]
. Five
. Six
[loweralpha]
.. a
.. b
.. c
. Seven
```

출력

```markdown
[lowerroman, start=5]
. Five
. Six
[loweralpha]
.. a
.. b
.. c
. Seven
```



#### 이름 붙은 목록(Labeled list)

항목에 대한 설명을 위해서 자주 사용된다.  
형식 `<이름>:: <내용>` , 옆으로 나오게 하고 싶으면` [horizontal]` 속성을 추가하면 된다.



- 구분자
- 공백
- 아이템

으로 구성된다

소스

```
CPU:: The brain of the computer.
Hard drive:: Permanent storage for operating system and/or user files.
RAM:: Temporarily stores information the CPU uses during operation.
Keyboard:: Used to enter text or control items on the screen.
Mouse:: Used to point to and select items on your computer screen.
Monitor:: Displays information in visual form using text and graphics.
```

출력

```markdown
CPU:: The brain of the computer.
Hard drive:: Permanent storage for operating system and/or user files.
RAM:: Temporarily stores information the CPU uses during operation.
Keyboard:: Used to enter text or control items on the screen.
Mouse:: Used to point to and select items on your computer screen.
Monitor:: Displays information in visual form using text and graphics.
```



설명이 아랫줄이 아니라 옆으로 나오게 할수 있습니다. (가로)

소스

```
[horizontal]
CPU:: The brain of the computer.
Hard drive:: Permanent storage for operating system and/or user files.
RAM:: Temporarily stores information the CPU uses during operation.
```

출력

```markdown
[horizontal]
CPU:: The brain of the computer.
Hard drive:: Permanent storage for operating system and/or user files.
RAM:: Temporarily stores information the CPU uses during operation.
```

 


하위에 목록을 추가할수 있다.

소스

```
Dairy::
* Milk
* Eggs
Bakery::
* Bread
Produce::
* Bananas
```

출력

```markdown
Dairy::
* Milk
* Eggs
Bakery::
* Bread
Produce::
* Bananas
```



중첩도 가능하고 섞어 쓰는것도 가능합니다.

소스

```
Operating Systems::
  Linux:::
    . Fedora
      * Desktop
    . Ubuntu
      * Desktop
      * Server
  BSD:::
    . FreeBSD
    . NetBSD

Cloud Providers::
  PaaS:::
    . OpenShift
    . CloudBees
  IaaS:::
    . Amazon EC2
    . Rackspace
```

출력

```markdown
Operating Systems::
  Linux:::
    . Fedora
      * Desktop
    . Ubuntu
      * Desktop
      * Server
  BSD:::
    . FreeBSD
    . NetBSD

Cloud Providers::
  PaaS:::
    . OpenShift
    . CloudBees
  IaaS:::
    . Amazon EC2
    . Rackspace
```



#### 복잡한 목록 내용

목록에 나와야 하는게 한줄짜리 문장만 사용할 수는 없다.  
기본적으로 목록은 문단 취급을 받는다. 따라서 줄 바꿈을 해도 이어서 써진다.

  


소스

```
* The header in AsciiDoc is optional, but if
it is used it must start with a document title.

* Optional Author and Revision information
immediately follows the header title.

* The document header must be separated from
  the remainder of the document by one or more
  blank lines and cannot contain blank lines.
```

출력

- The header in AsciiDoc is optional, but if it is used it must start with a document title.
- Optional Author and Revision information immediately follows the header title.
- The document header must be separated from the remainder of the document by one or more blank lines and cannot contain blank lines.



목록이 하나 이상의 문단으로 구성될수도 있다. 그때는 다음 문단 전에 `+`를 넣어준다.

소스

```
* The header in AsciiDoc must start with a document title.
+
The header is optional.
```

출력

- The header in AsciiDoc must start with a document title.

  The header is optional.



문단이 아닌 블럭도 추가할수 있습니다. 각 문단 또는 블럭은 `+` 로 계속 연결되어야 한다.

소스

```
* The header in AsciiDoc must start with a document title.
+
----
= Document Title
----
+
Keep in mind that the header is optional.

* Optional Author and Revision information immediately follows the header title.
+
----
= Document Title
Doc Writer <doc.writer@asciidoc.org>
v1.0, 2013-01-01
----
```

출력

- The header in AsciiDoc must start with a document title.

  ```
  = Document Title
  ```

  Keep in mind that the header is optional.

- Optional Author and Revision information immediately follows the header title.

  ```
  = Document Title
  Doc Writer <doc.writer@asciidoc.org>
  v1.0, 2013-01-01
  ```



이렇게 매번 `+` 붙이는게 불편하다면 오픈 블럭(open block)을 사용하면 된다.

소스

```
* The header in AsciiDoc must start with a document title.
+
--
Here's an example of a document title:

----
= Document Title
----

NOTE: The header is optional.
--
```

출력

```markdown
* The header in AsciiDoc must start with a document title.
+
--
Here's an example of a document title:

----
= Document Title
----

NOTE: The header is optional.
--
```



오픈 블럭을 이용해 다른 문서를 끼워넣을수도 있습니다.

소스

```
* list item
+
--
include::shared-content.adoc[]
--
```



### 링크와 이미지

URL은 자동 인식, 다음은 자동으로 인식되는 scheme 목록이다

* http
* https
* ftp
* irc
* mailto
* [email@email.com](mailto:email@email.com)



자동 링크 연결을 막고 싶다면 앞에 `/` 를 붙이면 된다.

Scheme를 보이고 싶지 않으면` hide-uri-scheme` 속성을 추가하면 된다

소스

```
\http://a.com // 링크 연결 막음
```



소스

```
:hide-uri-scheme:
http://a.com
```

출력

[a.com](http://a.com/)



#### link 매크로

링크와 관련된 다양한 옵션을 사용하고 싶으면 link 매크로를 사용하면 된다.

```
link:url[optional link text, optional target attribute, optional role attribute]
```



출력

```markdown

link:url[optional link text, optional target attribute, optional role attribute]
```



소스

```
search/link:https://ecosia.org[Ecosia]
```

출력

```markdown
search/link:https://ecosia.org[Ecosia]
```

​    


#### 내부 링크

아스키독 문서 내부로 링크를 하고 싶다면 <<id 또는 세션 제목>>을 사용  

소스

```
The section <<images>> describes how to insert images into your document.
```

출력

```markdown
The section <<images>> describes how to insert images into your document.
```



#### 이미지

`image::`를 이용해 이미지를 포함할수 있다.

소스

```
image::sunset.jpg[]
```

```
image::sunset.jpg[제목]
```

  


이미지 id, 제목, 크기등에 대한 정보도 줄수 있다.

소스

```
[#img-sunset]
.A mountain sunset
[link=http://www.flickr.com/photos/javh/5448336655]
image::sunset.jpg[Sunset,300,200]
```



### 제목 (Titles)

아스키독은 3가지 타입의 제목을 지원한다. 기본적으로 제목에는 = 를 사용해서 표현한다.

* 문서 제목
* 섹션 제목
* 블럭 제목



#### 문서 제목

문서 제목을 만들기 위해서는 문서의 첫 줄을 `= `로 시작해야 합니다.

소스

```
= Lightweight Markup Languages

According to Wikipedia...
```

문서 제목 다음은 문서에 대한 헤더 정보들이 나올수 있다.

- Line 1

  저자이름, 이메일 주소

- Line 2

  리비전, 날자, 기타

소스

```
= Lightweight Markup Languages
Doc Writer <doc.writer@asciidoc.org>
v1.0, 2012-01-01

According to Wikipedia...
```

출력

```markdown
= Lightweight Markup Languages
Doc Writer <doc.writer@asciidoc.org>
v1.0, 2012-01-01

According to Wikipedia...
```



#### 문서 속성(Document attributes)

문서 헤더 다음에는 속성을 기술할수 있다. 속성은 `:`으로 둘러쌓인 이름과 값으로 구성된다.

```
= User Guide
Doc Writer <doc.writer@asciidoc.org>
2012-01-01
:appversion: 1.0.0
```

이렇게 기술된 속성은 문서 내에서 괄호로 치환될수 있습니다. 이를 이용해 전역 변수 처럼 사용할수 있습니다.

```
The current version of the application is {appversion}
```

문서 속성은 asciidoc의 기능 플래그 설정용으로도 사용된다. 

* 예를 들어 목차 기능을 활성하고 싶다면 `toc` 속성을 주면 된다

```
:toc:
```

속성을 해제 하고 싶다면 `!`를 이름 뒤에 붙여서 표기하면 된다.

소스

```
:linkcss!:
```



Asciidoctor의 기본 설정값을 변경할때도 사용한다.

소스

```
:imagesdir: ./images
:iconsdir: ./icons
:stylesdir: ./styles
:scriptsdir: ./js
```

####  섹션 제목

`=` 를 이용해 세션 제목을 표현한다.  중첩 섹션이 될때마다 중첩 레벨이 증가합니다 (0-based입니다)

소스

```
= Document Title (Level 0)

== Level 1 Section

=== Level 2 Section

==== Level 3 Section

===== Level 4 Section

====== Level 5 Section

== Another Level 1 Section
```



제목에는 몇가지 중요한 규칙이 있다.

* 문서 타입이 book일 경우 하나의 문서에는 단 하나의 레벨 0 섹션이 있어야 한다. (기본 문서 타입은 article 이다 )

* 섹션 중첩 레벨을 뛰어넘기 할수 없다.

* 문서 제목 이후 첫 섹션 사이의 문장은 서문에 해당



소스

```
= Document Title

= Illegal Level 0 Section (violates rule #1)

== First Section

==== Illegal Nested Section (violates rule #2)
```

문서 제목 이후 첫 섹션 사이의 문장은 서문에 해당한다.

### 2.9. Block

다양한 기호가 의미 블럭을 구성합니다.

- `….`: 입력한 그대로 문자열이 생성.
- `----`: Source Code 블럭
- `|===`: 표(Table) 블럭
- `--`: 위치에 따라 적절한 블럭으로 동작. 단 passthrough, table 제외

섹션에 번호를 붙이고 싶다면 `setnums` 속성을 문서에 준다. .

```
:setnums:
```



#### 블럭 제목

블럭 위에 `.` 으로 시작하는 제목을 줄 수있다. (블럭은 문단, 목록, 다른 블럭 요소가 될수 있다.)

```
.TODO list
- Learn the AsciiDoc syntax
- Install AsciiDoc
- Write my document in AsciiDoc
```



```markdown
.TODO list
- Learn the AsciiDoc syntax
- Install AsciiDoc
- Write my document in AsciiDoc
```



### 제한 블럭(Delimited block)

블럭의 범위를 지정할수 있다. 



```
----
This is an example of a _listing block_.
The content inside is displayed as <pre> text.
----
```

  


```markdown
----
This is an example of a _listing block_.
The content inside is displayed as <pre> text.
----
```



* 이런 블럭 종류는 여러종류가 있다.

| 이름(스타일)   | 줄구분자 | 목적                                       | 치환     |
| -------------- | -------- | ------------------------------------------ | -------- |
| comment        | ////     | 출력되지 않을 설명을 기술하기 위해 사용    | none     |
| exmple         | ====     | 예제나 경고를 표기하기 위한 블럭           | normal   |
| literal        | ….       | 입력하 그대로 출력하기 위한 블럭           | verbatim |
| listing,source | ----     | 소스코드나 키보드입력을 표현하기 위함      | verbatim |
| open           | —        | 익명 블럭. 다른 블럭 역할을 수행할수 있음. | varies   |
| pass           |          | raw 문자열을 처리하지 않게한다             | none     |
| quote,verse    | *__*     | 인용문                                     | normal   |
| sidebar        | ****     | 문서 본문 옆                               | normal   |
| table          | \|===    | 테이블                                     | varies   |

### Tables

Table delimiter로 둘러 쌓아 테이블을 표시할수 있다. 각 컬럼은 |로 분기한다. 컬럼수는 속성으로 줄수 있다.

소스

```
[cols=2*]
|===
|Firefox
|Web Browser

|Ruby
|Programming Language

|TorqueBox
|Application Server
|===
```

출력

| Firefox   | Web Browser          |
| --------- | -------------------- |
| Ruby      | Programming Language |
| TorqueBox | Application Server   |



#### Tables Header

Table에 헤더를 주고 싶으면 options=header을 사용하면 된다.

소스

```
[cols=2*,options=header]
|===
|Name
|Group

|Firefox
|Web Browser

|Ruby
|Programming Language

|===
```

출력

| Name    | Group                |
| :------ | :------------------- |
| Firefox | Web Browser          |
| Ruby    | Programming Language |



#### 헤더는 단축 사용법 .

소스

```
[%header,cols=2*]
```

셀의 구분자는 `|` 이기 때문에 줄바꿈과 무관하다.. 줄바꿈을 통해 문단처럼 길게 이어지는 컨텐츠를 추가할수 있다.

```
|===
|Name |Group |Description

|Firefox
|Web Browser
|Mozilla Firefox is an open-source web browser.
It's designed for standards compliance,
performance, portability.

|Ruby
|Programming Language
|A programmer's best friend.
|===
```

  


| Name    | Group                | Description                                                  |
| :------ | :------------------- | :----------------------------------------------------------- |
| Firefox | Web Browser          | Mozilla Firefox is an open-source web browser. It’s designed for standards compliance, performance, portability. |
| Ruby    | Programming Language | A programmer’s best friend.                                  |

 

### 컬럼 너비조절

컬럼의 너비조절은 컬럼 cols에 줄 수 있다

```
[cols="2,3,5"]
|===
|Name |Group |Description

|Firefox
|Web Browser
|Mozilla Firefox is an open-source web browser.
It's designed for standards compliance,
performance and portability.

|Ruby
|Programming Language
|A programmer's best friend.

|===
```

출력

```markdown
[cols="2,3,5"]
|===
|Name |Group |Description

|Firefox
|Web Browser
|Mozilla Firefox is an open-source web browser.
It's designed for standards compliance,
performance and portability.

|Ruby
|Programming Language
|A programmer's best friend.

|===
```



#### 컬럼안에 asciidoc 컨텐츠를 추가

cols에 a옵션을 주면 된다.



```
[cols="2,3,5a"]
|===
|Name |Group |Description

|Firefox
|Web Browser
|Mozilla Firefox is an open-source web browser.
It's designed for:

* standards compliance,
* performance and
* portability.

|Ruby
|Programming Language
|A programmer's best friend.

|===
```

출력

```markdown
[cols="2,3,5a"]
|===
|Name |Group |Description

|Firefox
|Web Browser
|Mozilla Firefox is an open-source web browser.
It's designed for:

* standards compliance,
* performance and
* portability.

|Ruby
|Programming Language
|A programmer's best friend.

|===
```



또는 각 셀에 직접 아스키독 지원 여부를 추가할수 있다.

```
a|Mozilla Firefox is an open-source web browser.
It's designed for:

* standards compliance,
* performance and
* portability.
```



### CSV 표 만들기

CSV에서 표를 만들어 낼수 있다.

```
[%header,format=csv]
|===
Artist,Track,Genre
Baauer,Harlem Shake,Hip Hop
The Lumineers,Ho Hey,Folk Rock
|===
```



| Artist        | Track        | Genre     |
| :------------ | :----------- | :-------- |
| Baauer        | Harlem Shake | Hip Hop   |
| The Lumineers | Ho Hey       | Folk Rock |

  


CSV 파일을 읽어 들일수도 있다.

```markdown
[%header,format=csv]
|===
include::tracks.csv[]
|===
```

### Source Code

- [source] 블럭을 이용해 소스코드를 제공

- [source,javascript] 블럭을 이용해 문법강조할 `언어`를 지정

- [source,javascript,linenums] 블럭을 이용해 줄번호를 보여줄수 있다.

- include 매크로를 이용해 파일을 읽어와서 소스를 첨부할수 있다.

  ```markdown
  [source,ruby]
  ----
  include::app.rb[]
  ----
  ```



### Replacement

⇒ © 같은 기호를 표현하기 위해 => (C) 로 표기 하면 치환 된다. 이스케이핑을 위해 \=> 로 표기하면 변환이 되지 않는다.

```markdown
[source,ruby]
----
include::app.rb[]
----
```







### 참조

* https://narusas.github.io/2018/03/21/Asciidoc-basic.html
* https://asciidoctor.org/docs/what-is-asciidoc/

* https://www.baeldung.com/asciidoctor
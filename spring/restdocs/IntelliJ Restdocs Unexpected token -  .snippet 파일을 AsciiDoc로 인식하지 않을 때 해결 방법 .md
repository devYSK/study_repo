# IntelliJ Restdocs Unexpected token -  .snippet 파일을 AsciiDoc로 인식하지 않을 때 해결 방법 



Restdocs 커스텀을 위하여 src/test/resources/org/springframework/restdocs/templates 경로에 snippet 파일을 추가하여 작성하면 다음처럼 인식이 되지 않아서 작성하기 힘든 경우가 생긴다.

> Unexpected token 

빨간줄에, 문법 형식도 맞지 않게 작성된다. 

<img src="https://blog.kakaocdn.net/dn/Gv7mL/btrXnKfszgD/KHQFx4awgJON9T2wl0e4YK/img.png" width = 600 height = 350>



Intellij 설정을 바꾸면 해결된다.

## 해결법

> Mac 기준
>
> Preferences -> Editor -> File Types -> Recognized File Types 
>
> 
>
> Recognized File Types 에서 마우스 조금 내리다보면 AsciiDoc files보인다.
>
> File name patterns에 *.snippet 추가

<img src="https://blog.kakaocdn.net/dn/cSAqzk/btrXo5pH7M9/p0a9Txdw4ZKzfqmLGpV0A0/img.png" width = 800 height=550>

writing AsciiDoc works best with soft-wrap enabled. Do you want to enable it by default? 라는 문구 발견

* AsciiDoc 작성은 소프트 랩이 활성화된 상태에서 가장 잘 작동합니다. 기본적으로 활성화하시겠습니까?

활성화 창 클릭 

<img src="https://blog.kakaocdn.net/dn/stDux/btrXn9e3qyZ/FKZmvPjEivXk2PWBr1MAK1/img.png" width = 600 height=350>



yes, take me to the Soft Wrap settings! 클릭 

<img src="https://blog.kakaocdn.net/dn/E7xJQ/btrXpcbiDK3/3igMBTspAjkdiiyku2kF2k/img.png" width = 700 height = 300>

soft-wrap these fiels -> *.snippet; 추가 

<img src="https://blog.kakaocdn.net/dn/nkBD3/btrXmSrPEzm/CKWN04Mck0j4mC88nMkWP0/img.png" width= 800 height = 550>



문제없이 asciidoc 작성을 할 수 있다. 
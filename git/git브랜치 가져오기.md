# git branch(브랜치): 가져오기, 합치기, 삭제하기



다음 실습은 mac 터미널 기준으로 작성되었습니다.



## 원격 브랜치 가져오기 

* remote(원격) 저장소 (github, bitbucket, gitlab) 등에서 로컬 저장소(내 로컬 컴퓨터)로 가져오는 방법
* 만약 원격 저장소에 main(master), develop, feature1 등의 브랜치가 있을 때
  * remote 저장소에서 `clone`, `pull` 하면 main(master) 브랜치만 받아와지고 develop, feature1 등의 브랜치는 받아와 지지 않는다.
* 원격 저장소의 특정 branch (develop, feature)를 가져오기 위해서는 `git checkout -t` 명령어를 사용해야 한다



## 1. 원격 저장소 갱신

먼저 원격 저장소의 브랜치에 접근하기 위해 로컬에서 다음 명령어를 입력한다.

```
git remote update
```


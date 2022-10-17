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

* 원격 저장소 갱신 명령어이다



## 2. 원격 저장소 branch 확인

* 원격 저장소의 branch가 뭐뭐 있는지 리스트(list)를 확인해야 한다

### 명령어

* -r : 원격 옵션 : 리모트 추적 브랜치에 대해 목록을 모두 표시
* -a: 로컬, 원격 전체 옵션 : 리모트와 로컬의 브랜치 목록을 모두 표시 

```git
git branch -r
```

* 리모트 저장소에 있는 branch list를 모두 확인할 수 있다.



```
// 아래와 같이 branch 목록이 다음과 같이 뜨며, 창을 끌라면 q를 입력하면 된다
origin/HEAD -> origin/main
origin/develop
origin/feature
origin/main
```



## 3. 원격 저장소에 있는 branch와 이름이 중복되는 로컬 저장소 branch 제거

만일, 원격 저장소 branch와 동일한 이름의 로컬 branch가 있는 경우, 병합 관련 에러가 있을 수 있는데,

이때 로컬 저장소의 branch를 제거하고 싶다면 `git branch -d 브랜치명` 을 입력하여 제거한다

```
git branch -d 브랜치명
```



## 4. 원격 저장소의 branch 가져오기

`git branch -t 브랜치명` 을 사용하면, 원격 저장소의 branch 이름과 동일한 이름으로 로컬 저장소에 branch를 만들고, 그 branch로 checkout을 한다.

### 명령어 옵션

* -t : 원격 branch 이름과 동일한 이름으로 로컬에 branch 생성하고, 그 branch로 checkout
* -b : b를 사용하면 원격 branch를 local에 이름을 변경하여 가져온다
* -f : 문제가 발생해도 강제로 branch이름과 동일한 로컬 저장소 branch를 생성하고, 해당 branch로 checkout

> origin 을 명시해서 붙여줘야 한다.

```
git checkout -t origin/develop
// 또는
git checkout -f -t origin/develop
```





# 브랜치 합치기, 병합하기

저장소를 만들면 기본적으로 master(main) branch가 생성된다.  

보통 개발 프로세스에서 다른 branch에서 개발을 하고, develop 또는 main branch로 병합하게 된다  

`checkout` 명령을 사용하여 기존의 branch를 new branch 라는 이름의 branch로 변경하는데,   
이 때 `-b` 옵션을 사용해 해당 이름의 새로운 branch를 생성한다. 

```
git checkout -b newbranch // newbranch로 변경
git branch -d <new branch> <원격 branch 이름>
```



해당 newbranch에서 개발을 진행한 이후, 작업한 branch를 원격 저장소에 `push` 하여 저장한다.

```
git push origin newbranch
```



작업이 완료되면 main 브랜치로 돌아온다.

 ``` git checkout main ``` 

newbranch를 main에 merge한다. 


``` git merge newbranch ```



완료되면 다음과 같이 fast forward 메시지와 함께 main이 newbranch로 이동하게 된다.

> Updating ......
> Fast-forward
> myfile.txt | 1+
> 1 files changed ...



# 로컬, 원격 브랜치 제거

git에서 로컬 브랜치와 원격 브랜치를 제거하는 방법  


삭제할 브랜치 이름은 feature2 이다.



## 1. 원격 저장소의 branch 제거

원격 저장소 (origin)에만 feature2가 존재하고, local 저장소에는 없는 경우에는
git push 명령어를 이용해서 원격 저장소에 `push`하고` delete` 명령어를 추가하여 브랜치를 삭제할 수 있다.

```
git push origin --delete feature2
```



## 2. 로컬 및 원격 저장소의 branch 제거

로컬 및 원격 저장소의 branch를 모두 삭제할 경우, 
`branch -d` 명령으로 local branch를 삭제하고 원격 저장소에 `push` 한다

```
git branch -d feature2
git push origin feature2
```


# CI/CD란

매번 개발자가 코드를 수정하고 빌드와 테스트를 하고 배포까지 한다면 상당히 많은 시간이 소요된다. 

그러나 빌드와 테스트, 배포과정을 개발자가 직접하는것이 아니라면 쓸데없는 시간을 단축시키고 개발에 더 많은 시간을 투자할 수 있다.

CI/CD는 애플리케이션 개발팀이 더 자주, 안정적으로 코드 변경을  좀 더 효율적이고 빠르게 사용자에게 제공하기 위해 사용한다.

<br>

CI/CD의 개념만으로는 `자동화`와 직접적으로 관련이 있지는 않다. 

그럼에도 자동화라는 키워드는 CI/CD라는 단어에 거의 항상 따라붙는다. 

또한 CI/CD는 DevOps 엔지니어의 핵심 업무라고 불리기도 한다.

이 부분을 도와주는 CI / CD의 개념에 대하여 정리하고자 한다.

<br>

### CI (Continuous integration, CI) : 지속적 통합

애플리케이션의 버그 수정이나 코드의 새로운 변경이 생기면 수시로 빌드 및 테스트등을 하면서

` ‘코드에대한 통합’`을 `‘지속적’`으로 진행함으로써 품질을 유지하는것.

애플리케이션의 버그 수정이나 새로운 코드 변경이 주기적으로 빌드 및 테스트되면서 공유되는 git repository에 `통합`되는 것을 의미한다.

Git에 특정 브랜치(master, develop)에 새로운 커밋이 될 때 마다, 해당 코드를  빌드하고 테스트 코드를 실행하여 문제가 없는지를 체크하는 과정이 자동화된다.



### 필요성 

대부분의 현대 애플리케이션에서는 다양한 플랫폼과 툴을 사용해 코드를 개발해야 하므로 팀은 변경을 통합하고 검증할 일관적인 메커니즘이 필요하다.

* 지속적 통합은 애플리케이션을 빌드, 패키징, 테스트하기 위한 자동화된 방법을 구축한다. 
* 모든 개발이 끝난 이후에 코드 품질을 관리하는 고전적 방식의 단점을 해소하기위해 나타난 개념이다.
* 코드의 검증에 들어가는 시간이 줄어든다.
* 개발 편의성이 증가한다.
* 항상 테스트 코드를 통과한 코드만이 레포지터리에 올라가기 때문에, 좋은 코드 퀄리티를 유지할 수 있다.



git repository에 코드만 올리고 나머지 작업인 테스트와 빌드는 프로그램이 자동으로 실행.

자동화라는 키워드가 CI/CD의 개념만을 보자면 직접적으로 관련이 없어도, 자동화라는 키워드가 떨어지지 않는 이유이다.

똑같이 반복되는 귀찮은 작업을 하지 않을 수 있으며, 문제도 더 줄일 수 있는 방법

빌드 및 테스트는 자동으로 진행되므로, 버그가 생기면 확인해서 버그를 해결한다.



### CI 과정

1.  코드를 통합한다.
   

2.  통합한 코드가 제대로 동작하는지 테스트한다. 
   

3.  제대로 빌드가 되는지도 테스트한다.
   

4.  결과를 정리하고 버그가 존재한다면 해결한다

   

<br>

### CD (Continuous delivery, CD) : 지속적 제공, 배포

`배포 자동화 과정`을 의미하는 용어로 `지속적 서비스 제공(Continuous Delivery)` 또는 `지속적 배포(Continuous Deployment)`를 의미한다.

CI가 끝나면 다음 목적은, 항상 최신의 `좋은 코드`들을 `서버에 배포`하는 것. 

코드 변경의 파이프라인의 이전 단계를 모두 성공적으로 통과하면, 수동 개입 없이 해당 변경 사항을 자동으로 배포한다. 

지속적 배포를 채택하면 품질 저하 없이 `최대한 빨리` 사용자에게 `새로운 기능을 제공`할 수 있다.

<br>

CI의 과정이 되어 있다면, 우리는 배포도 CI가 완료되는 시점에 자동으로 실행하면 된다. 

지속적 제공은 지속적 통합이 끝나는 지점부터 시작되며, 프로덕션, 개발, 테스트 환경을 포함해 선택한 환경으로 애플리케이션을 제공하는 과정을 자동화한다. 
지속적 제공은 이런 환경으로 코드 변경을 적용(push)하기 위한 자동화된 방법이다. 

즉, CI의 연장선이며 배포 이전에 테스트와 빌드는 필수적이기 때문에 사실상 CD가 되려면 항상 CI가 선행되어야 한다

<br>

**CD를 적용한 후의 흐름**

> 1. CI를 적용하여 Bulid 되고 Test 된 후에, 배포 단계에서 release 할 준비 단계를 거치고 문제가 없는지 검증
> 2. 배포 환경과 비슷한 곳 (develop, test 환경 등) 에서 검증을 진행한다.
> 3.  검증된 소프트웨어를 자동화를 통하여 실제 프로덕션 환경으로 배포한다.

* 개발자를 배포보다는 개발에 더욱 신경 쓸 수 있도록 도와준다.
  

* 개발자가 원클릭으로 수작업 없이 빌드, 테스트, 배포까지의 자동화를 할 수 있다.  

CI/CD는 어느 정도의 자동화를 하냐에 따라 조금씩 다르기 때문에, CI/CD라고 해서 모두 같은 게 아니라 회사마다 혹은 팀마다 어느 정도 다를 수 있다.

 

 

![배경없는 CI:CD](https://user-images.githubusercontent.com/59376200/131976807-f2d680a3-c9ae-4633-b37c-5cdc92209460.png)

* 그림 - CI / CD 과정
  

### [**CD의 장점**]

- 개발자는 배포보다는 개발에 더욱 신경 쓸 수 있도록 도와준다.
  
- 개발자가 원클릭으로 수작업 없이 빌드, 테스트, 배포까지의 자동화를 할 수 있다.
  



- CI/CD 적용 전  
  

  > 1) 코드를 수정/추가 등을 하며 개발을 진행한다.
  > 2) 각자의 코딩 컨벤션에 따라 브랜치에 push 한다.(여기서 에러가 발생했는지 아닌지 판단할 수 없음
  > 3) 해당 브랜치 코드에 문제가 없다고 판단되면 main branch에 병합한다.
  > 4) 에러가 발생했다면 1)~3) 과정으로 에러를 고치고, 에러가 발생하지 않았다면 직접 배포를 진행한다

- CI/CD 적용 후  
  

  > 1) push된 코드를 CI가 알아서 Build, Test, Lint(포맷팅)을 실행하고 결과를 알려준다.
  > 2)  개발자들은 결과를 보고 에러가 난 부분을 수정한 후 main branch에 병합한다.
  > 3)  main branch를 감지하고 있던 CD 과정이 알아서 배포를 수행한다.



1. 버전 제어에서 코드를 풀링해서 빌드 실행 
   

2. 코드로 자동화된 필요 인프라 단계를 실행해 클라우드 인프라 구축 또는 해체 
   

3. 타깃 컴퓨팅 환경으로 코드 이동
    

4. 환경 변수를 관리하고 타겟 환경에 맞게 구성
    

5. 애플리케이션 구성요소를 웹 서버, API, 데이터베이스 서비스와 같은 적절한 서비스로 푸시
    

6. 새 코드 푸시에 필요한 서비스 엔드포인트를 호출하거나 서비스를 재시작하기 위해 필요한 단계 실행 
   

7. 지속적 테스트 실행과 테스트 실패 시 롤백 
   

8. 제공 상태에 대한 로그 데이터 및 알림 제공 
   

9. 구성 관리 데이터베이스를 업데이트하고 IT 서비스 관리 워크플로우에 완료된 배포에 대한 알림 전송 
   

* 지속적 배포를 사용하여 프로덕션으로 배포하는 팀은 다운타임을 최소화하고 배포 위험을 관리하기 위해 다양한 방법을 사용할 수 있다. 
*  한 가지 옵션은 이전 소프트웨어 버전에서 새 버전으로 트래픽 사용의 전환을 조율하는 카나리 배포를 구성하는 것이다. 



### 쿠버네티스 및 서버리스 아키텍처에서의 CI/CD 

클라우드 환경에서 CI/CD 파이프라인을 운영하는 많은 팀은 도커와 같은 컨테이너, 쿠버네티스와 같은 오케스트레이션 시스템도 사용한다. 

컨테이너는 이식 가능한 표준적 방식으로 애플리케이션을 패키징하고 배포할 수 있게 해준다. 

컨테이너를 사용하면 변화하는 워크로드에 따라 환경을 확장하거나 해체하기가 쉽다. 

컨테이너, 코드형 인프라(IaC), CI/CD 파이프라인을 함께 사용하는 접근법은 많다. 

젠킨스와 함께 쿠버네티스 사용하기, 애저 데브옵스와 함께 쿠버네티스 사용하기와 같은 무료 학습 자료를 살펴보는 것도 도움이 될 것이다. 

또 다른 방법은 서버리스 아키텍처를 사용해서 애플리케이션을 배포 및 확장하는 것이다. 

서버리스 환경에서는 클라우드 서비스 업체가 인프라를 관리하고, 애플리케이션은 구성을 기반으로 필요에 따라 리소스를 소비한다. 

예를 들어 AWS에서 서버리스 애플리케이션은 람다 함수로 실행되고 플러그인을 사용해 젠킨스 CI/CD 파이프라인에 배포를 통합할 수 있다. 

애저 서버리스 및 GPS 서버리스 컴퓨팅도 비슷한 서비스다. 
 

### 차세대 CI/CD 애플리케이션 

CI/CD 파이프라인 개발 및 관리를 위한 더 고급 영역에 관심이 있다면 다음을 참고할 만하다.  
 

- MLOps는 머신러닝 모델의 IaC 및 CI/CD이다. 인프라, 통합, 그리고 학습 및 프로덕션 환경으로의 배포를 지원한다. 
- 합성 데이터 생성(Synthetic data generation) 기법은 테스트 자동화 엔지니어가 API를 테스트하는 용도로, 그리고 데이터 과학자가 모델을 교육시키는 용도로 사용하는 데이터 집합을 머신러닝을 사용해서 생성한다. 
- AIOps 플랫폼(또는 ITOps의 머신러닝 및 자동화)은 관찰 가능성 데이터를 집계하고 여러 소스의 알림을 인시던트로 연계한다. 자동화는 필요에 따라 CI/CD 배포와 롤백을 트리거할 수 있다. 
- 마이크로서비스를 다루는 팀은 재사용 가능한 파이프라인을 생성해 애저 및 AWS에서 개발, 검토 옵션을 지원하고 확장한다. 
- 엔지니어는 네트워크 구성, 임베디드 시스템, 데이터베이스 변경, IoT, AR/VR과 같은 다른 영역에서 CI/CD를 사용한다.



## [**CI/CD 플랫폼 및 종류 **]

* Jenkins  
  

* TravisCI 
  

* Github Actions 
  

* CircleCI 
  

- Bamboo 
  
- etc 
  



### Jenkins

- 무료. Reference 및 사용자층과 정보가 많음
- Windows, macOS 또는 openSUSE, Red Hat, Ubuntu와 같은 다양한 리눅스 OS에 사용가능
- 설치 및 사용이 간단하다.
- 발전하는 플러그인 생태계 (플러그인은 젠킨스내에서 여러 유용한 기능을 제공하고 생산성과 안정성을 높이게끔 해주는 역할을 함)
  - 1,800개 이상의 플러그인이 있다.

###  Bamboo

- Atlassian에서 개발한 CI 도구로 JIRA 의 대쉬보드나 confluence의 Page에 bamboo build chart 를 붙일수도 있고 JIRA 의 특정 이슈와 관련된 build 내역을 조회하는 등 atlassian 제품을 기존에 사용하고 있다면 각 제품군을 통합해서 더욱 유기적으로 사용할 수 있음.
- 유료 서비스
- Windows, Mac OS X, Linux와 같은 플랫폼에서 사용가능

### Travis CI

- 간결하고 직관적인 웹 인터페이스를 제공하는 인터넷 기반의 CI 서비스
- 무료 버전과 기업용 버전(유료)이 있음
  - 오픈 소스 프로젝트 일 경우(public repo) 무료로 사용 가능
  - private repo일 경우 한달에 69$
- Github와 연동하여 Commit/Push를 기반으로 CI가 자동 동작하며, Push 외에도 Pull Request에도 반응하도록 설계되어 있음

### Circle CI

- Linux, macOS, Android, and Windows 운영체제에서 사용 가능
- 일정 한도 내에서 무료로 사용 가능
  - 한주에 2500 크레딧 부여하고 한번에 하나의 job 수행
  - 무료 제공되는 크레딧으로 한달에 1000분 정도의 빌드시간 사용 가능
  - 유료로 사용할 경우 한달에 15달러
- Git에 Push를 하면 자동으로 테스트를 진행하여 검사 결과를 알려 주어 문제점에 대해 바로 알림을 보내줌. 이를 통해 협업시 발생할 수 있는 파일충돌이나 에러를 잡을 수 있음. 그리고 문제가 없다면 서버에 배포까지 자동으로 이루어짐



* [젠킨스 관련 좋은 글](https://www.itworld.co.kr/news/107527)







### 참조



https://startupdevelopers.tistory.com/237

https://itholic.github.io/qa-cicd/

[https://www.ciokorea.com/insider/233289#csidx87421f307100b49b84ca42c0d9f5a25 ](https://www.ciokorea.com/insider/233289#csidx87421f307100b49b84ca42c0d9f5a25)![img](http://linkback.itworld.co.kr/images/onebyone.gif?action_id=87421f307100b49b84ca42c0d9f5a25)

https://velog.io/@choi-ju12g/%EC%BB%B4%ED%8C%8C%EC%9D%BC-%EB%B9%8C%EB%93%9C-%EB%B0%B0%ED%8F%AC-%EA%B0%9C%EB%85%90-%EB%B0%8F-CICD-%EA%B0%9C%EB%85%90-%EC%A0%95%EB%A6%AC
# 인프런 부하테스트 입문/실전

[toc]

* 자료 : https://jscode.notion.site/14211062ff0780c2a77ee2f770da510a

* https://github.com/JSCODE-COURSE/load-testing-server/tree/main/src/main/java/com/example/demo

# 처리량(Throughput), 지연 시간(Latency)

처리량 : 초당 처리할 수 있는 트래픽 양. 보통 초당 트랜잭션 수(TPS)로 측정. 쓰루풋 이라고도 함

 TPS(Transaction Per Seconds) ≒ RPS(Request Per Second)



# 부하테스트 툴 k6



* https://san-tiger.tistory.com/entry/K6%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%84%9C%EB%B2%84-%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9D%B4%EC%8A%88
* https://kinggodgeneral.tistory.com/72

맥에서 k6를 돌리는것은 별로 좋지 않다.



EC2에 부하테스트 툴 셋팅하기

```
$ sudo gpg -k && /
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69 && /
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list && /
sudo apt-get update && /
sudo apt-get install k6
```

로키리눅스에 k6 설치

```
curl -LO https://github.com/grafana/k6/releases/download/v0.55.2/k6-v0.55.2-linux-amd64.tar.gz

tar -xvf k6-v0.55.2-linux-amd64.tar.gz

sudo mv k6-v0.55.2-linux-amd64/k6 /usr/local/bin/

```





## 부하 테스트의 흐름

사용자들이 현재 시스템상 몰렸을때 서버가 터질까?

이 기능은 어느정도 요청을 견딜 수 있을까



1. 부하테스트 필요성 인식
   * 사용자들이 현재 시스템상 몰렸을때 서버가 터질까?
   * 이 기능은 어느정도 요청을 견딜 수 있을까

2. 부하테스트 목표 설정하기

   * 부하 테스트 목표는 주로 **Throughput**과 **Latency**를 활용해 설정한다.

   * **목표 Throughput : 2000TPS**

     → 서비스에 예상 접속자 수, 예상 요청 수 등을 활용해 TPS 목표를 설정한다.

   * **평균 Latency : 800ms**

     → 서비스의 특성에 맞게 Latency를 설정한다.

3. 시스템이 어느정도 트래픽을 견딜 수 있는지 부하 테스트 진행
   * 부하의 정도를 올려가면서 어느 정도 부하까지 견딜 수 있는 지를 측정한다. 즉, 시스템의 최대 Throughput을 측정한다. 

4. 병목 지점 파악 후 성능 개선하
   * 목표로 설정한 Throughput, Latency를 달성하기 위해, 기존 시스템의 성능을 개선해야 할 수도 있다. 성능 개선을 하려면 가장 먼저 병목 지점을 파악해서 개선해야 한다.
5. 다시 개선한 시스템이 어느정도 트래픽까지 견디느지 부하테스트 진행
   * 개선한 시스템이 어느 정도 트래픽까지 견딜 수 있는 지 부하 테스트를 진행한다. 부하 테스트를 통해 최대 Throughput과 평균 Latency를 측정해서 목표치를 달성했는 지 체크한다.
   * 만약 목표치를 달성하지 못했다면 다시 4번 과정(병목 지점 파악 후 성능 개선)을 거친 후 부하 테스트를 진행해서 목표치 달성 여부를 확인한다. 이 과정을 목표 달성 때까지 반복한다.



## 부하테스트 주의점

1. 최소 5분~1시간은 부하테스트를 진행해야 일관된 결괏값을 얻을 수 있다.
2. 프로덕션 환경과 비슷한 데이터를 세팅해야 한다. DB 복제본을 뜨는것이 좋다
3. 프로덕션과 분리된 환경이지만 최대한 비슷한 환경에서 테스트를 진행한다.





## AWS지표 설정하기

* ec2 https://jscode.notion.site/CPU-15411062ff0780108be4cd079fde51e0

* rds : https://jscode.notion.site/RDS-15611062ff07806a99affbeb6a4b77bc*



# DB 병목지점 해결하기

DB를 성능 개선 할 때는 크게 4가지 방향성이 있다고 설명했었다. 그 중 비효율적인 쿼리 개선을 가장 먼저 시도해봐야 한다.

1. **비효율적인 쿼리 개선하기 (인덱스 활용, SQL문 튜닝, 역정규화 등)**
2. 수직적 확장
3. 읽기 전용 데이터베이스(Read Replica) 도입하기
4. 캐시 서버 도입하기




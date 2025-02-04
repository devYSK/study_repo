# k6 stress 테스트

* https://www.udemy.com/course/k6-load-testing-performance-testing/learn/lecture/39872260?start=0#overview



* docs : https://grafana.com/docs/k6/latest/



# K6 대시보드 추천

* 14796번
* 2587번 (k6 Load Testing Results) 가장 괜찮음
* 13719 : By Groups 



# 로드 테스팅 메니페스토

- 로드 테스트 메니페스토는 성능, 로드테스팅을 깊이있게 수행한 결과이다.
- 성능 테스트를 올바르게 수행하기 위한 가이드를 다음과 같이 제공한다.
  - [Simple testing is better than no testing](https://k6.io/our-beliefs/#simple-testing-is-better-than-no-testing)
  - [Load testing should be goal oriented](https://k6.io/our-beliefs/#load-testing-should-be-goal-oriented)
  - [Load testing by developers](https://k6.io/our-beliefs/#load-testing-by-developers)
  - [Developer experience is super important](https://k6.io/our-beliefs/#developer-experience-is-super-important)
  - [Load test in a pre-production environment](https://k6.io/our-beliefs/#load-test-in-a-pre-production-environment)



# 외부 출력

- 출력 결과를 visualize 하기 위해서 결과 정보를 내보낼 수 있다.
- --out 플래그를 이용하여 원하는 대상을 지정할 수 있다.

```
k6 run --out statsd script.js
```

- 가능한 출력 옵션

  - Amazon CloudWatch: https://k6.io/docs/results-visualization/amazon-cloudwatch
  - Cloud: https://k6.io/docs/results-visualization/cloud
  - CSV: https://k6.io/docs/results-visualization/csv
  - Datadog: https://k6.io/docs/results-visualization/datadog
  - Grafana Cloud: https://k6.io/docs/results-visualization/grafana-cloud
  - InfluxDB: https://k6.io/docs/results-visualization/influxdb-+-grafana
  - JSON: https://k6.io/docs/results-visualization/json
  - Netdata: https://k6.io/docs/results-visualization/netdata
  - Net Relic: https://k6.io/docs/results-visualization/new-relic
  - Prometheus: https://k6.io/docs/results-visualization/prometheus
  - TimescaleDB: https://k6.io/docs/results-visualization/timescaledb
  - StatsD: https://k6.io/docs/results-visualization/statsd

- #### 복수 출력

  ```
  k6 run \
  --out json=test.json \
  --out influxdb=http://localhost:8086/k6
  ```

  - 위와 같이 복수형태로 출력이 가능하다.

# 성능 테스트 용어

------

### 1. **성능 테스트 (Performance Test)**

- **목적**: 시스템의 성능 특성을 측정하고, 정해진 기준이나 요구사항에 따라 동작하는지 확인합니다.
- 주요 목표
  - 응답 시간 (Response Time)
  - 처리량 (Throughput)
  - 시스템 자원 사용률 (CPU, 메모리 등)
- **사용 시점**: 시스템이 안정적으로 동작한다고 판단될 때 성능 최적화를 위해 수행.
- 예시
  - 특정 동시 사용자 수에 대한 응답 시간 측정.
  - 트랜잭션 처리 속도 확인.

------

### 2. **스트레스 테스트 (Stress Test)**

- **목적**: 시스템이 정상적인 사용량보다 높은 부하 상태에서 어떻게 동작하는지 확인합니다.
- 주요 목표
  - 한계를 넘어섰을 때 시스템의 안정성 테스트.
  - 고부하 상태에서 에러 처리 및 복구 능력 확인.
- **사용 시점**: 예상 부하 이상의 트래픽, 데이터 처리량, 사용자 수 등을 시뮬레이션.
- 예시
  - 정상 사용자 수의 2배 트래픽을 시뮬레이션해 에러 발생 여부 점검.
  - 네트워크 대역폭 한계를 초과한 요청 처리 테스트.

------

### 3. **스모크 테스트 (Smoke Test)**

- **목적**: 소프트웨어의 가장 기본적인 기능이 정상적으로 동작하는지 빠르게 확인합니다.

- 주요 목표

  - 새로운 빌드가 테스트 가능 상태인지 검증.
  - 주된 기능에 심각한 결함이 없는지 확인.

- **사용 시점**: 새로운 빌드 배포 직후, 본격적인 테스트 전에 수행.

- 예시

  :

  - 로그인 기능이 동작하는지.
  - 핵심 API가 정상적으로 호출되는지 확인.

------

### 4. **스파이크 테스트 (Spike Test)**

- **목적**: 갑작스러운 부하(트래픽 급증)에 대해 시스템이 어떻게 반응하는지 확인합니다.
- 주요 목표
  - 순간적인 트래픽 폭증 대응 능력 평가.
  - 급격한 트래픽 증가 후 시스템이 정상적으로 복구되는지 확인.
- **사용 시점**: 예상치 못한 부하 상황(프로모션, 이벤트 등)을 대비할 때.
- 예시
  - 초당 요청 수가 급증했을 때 서버가 정상 작동 여부.
  - 급증 후 정상 트래픽으로 돌아왔을 때 안정성 확인.

------

### 5. **브레이크포인트 테스트 (Breakpoint Test)**

- **목적**: 시스템이 정상 작동을 멈추는 한계점을 식별합니다.

- 주요 목표

  :

  - 시스템이 실패하거나 충돌을 일으키는 지점을 파악.
  - 한계치를 넘기기 전의 최대 처리량 확인.

- **사용 시점**: 시스템의 용량과 안정성을 측정할 때.

- 예시

  :

  - 동시 사용자 수를 단계적으로 증가시켜 서버 다운 시점을 기록.
  - DB의 최대 연결 한계를 측정.

------

### 6. **Soak 테스트 (지속 테스트, Soak Test)**

- **목적**: 오랜 시간 동안 지속적으로 부하를 주어 시스템의 장기적인 안정성을 테스트합니다.

- 주요 목표

  :

  - 메모리 누수, 자원 고갈 등의 문제 탐지.
  - 시스템이 지속적으로 높은 부하를 견딜 수 있는지 확인.

- **사용 시점**: 시스템의 장기적인 안정성을 확인할 필요가 있을 때.

- 예시

  :

  - 24시간 동안 지속적으로 트래픽을 가해 시스템 성능 측정.
  - 밤새 테스트를 돌려 메모리나 CPU 사용 패턴을 분석.

------

### 요약 표

| 테스트 종류               | 목적                                                 | 사용 시점                                             |
| ------------------------- | ---------------------------------------------------- | ----------------------------------------------------- |
| **성능 테스트**           | 시스템 성능 특성 측정 (응답 시간, 처리량 등)         | 시스템 안정성 확인 후 최적화 진행 시                  |
| **스트레스 테스트**       | 시스템이 정상 사용량을 초과할 때 안정성을 테스트     | 예상 부하 이상의 트래픽 또는 데이터 처리량 시뮬레이션 |
| **스모크 테스트**         | 소프트웨어 기본 기능이 정상 작동하는지 빠르게 확인   | 새로운 빌드 배포 직후 테스트 가능성 확인              |
| **스파이크 테스트**       | 갑작스러운 부하 증가 시 시스템 반응과 복구 능력 확인 | 이벤트, 프로모션 등으로 트래픽 급증 예상 시           |
| **브레이크포인트 테스트** | 시스템이 정상 작동을 멈추는 한계점을 식별            | 용량 측정, 최대 처리량 확인 시                        |
| **Soak 테스트**           | 장시간 부하 상태에서의 안정성 및 자원 고갈 문제 확인 | 장기적인 안정성 검증 필요 시                          |

------

### 추가 팁

이 테스트들은 상황에 따라 혼합해서 활용할 수도 있습니다. 예를 들어, 스트레스 테스트와 Soak 테스트를 조합해 장시간 고부하 환경에서의 안정성을 확인할 수 있습니다. 필요에 맞는 테스트 전략을 설계하는 것이 중요합니다.



mac os k6 설치

```
brew install k6

k6 version
```

* https://grafana.com/docs/k6/latest/set-up/install-k6/



first script

```js
import http from 'k6/http';

export default function () {
  const res = http.get('https://test.k6.io');
  check(res, { 'status was 200': (r) => r.status === 200 });
}
```



k6에서 소규모 테스트를 위해 제공하는 사이트

* https://test.k6.io/



```
     execution: local
        script: sample.js
        output: -

     scenarios: (100.00%) 1 scenario, 50 max VUs, 50s max duration (incl. graceful stop):
              * default: Up to 50 looping VUs for 20s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)


     ✓ HTTP/1.1 상태 코드가 200인지 확인
     ✓ HTTP/2 상태 코드가 200인지 확인

     checks.........................: 100.00% 1152 out of 1152
     data_received..................: 2.0 MB  94 kB/s
     data_sent......................: 75 kB   3.6 kB/s
     http_req_blocked...............: avg=6.91ms     min=0s       med=1µs      max=459.37ms p(90)=1µs      p(95)=2µs      
     http_req_connecting............: avg=3.11ms     min=0s       med=0s       max=257.49ms p(90)=0s       p(95)=0s       
   ✗ http_req_duration..............: avg=178.21ms   min=132.25ms med=155.35ms max=509.27ms p(90)=254.57ms p(95)=317.95ms 
       { expected_response:true }...: avg=178.21ms   min=132.25ms med=155.35ms max=509.27ms p(90)=254.57ms p(95)=317.95ms 
     http_req_failed................: 0.00%   0 out of 1152
     http_req_receiving.............: avg=517µs      min=6µs      med=69µs     max=25.79ms  p(90)=545.8µs  p(95)=3.01ms   
     http_req_sending...............: avg=145.89µs   min=10µs     med=78µs     max=5.47ms   p(90)=222.8µs  p(95)=383µs    
     http_req_tls_handshaking.......: avg=3.65ms     min=0s       med=0s       max=319.59ms p(90)=0s       p(95)=0s       
     http_req_waiting...............: avg=177.55ms   min=132.12ms med=154.57ms max=508.46ms p(90)=254.17ms p(95)=317.7ms  
     http_reqs......................: 1152    55.563937/s
     http1_req_duration.............: avg=189.292092 min=133.484  med=156.505  max=509.275  p(90)=298.181  p(95)=346.89875
     http2_req_duration.............: avg=167.138009 min=132.259  med=154.2985 max=439.104  p(90)=197.32   p(95)=242.573  
     iteration_duration.............: avg=1.37s      min=1.27s    med=1.31s    max=2.22s    p(90)=1.5s     p(95)=1.59s    
     iterations.....................: 576     27.781969/s
     vus............................: 7       min=7            max=50
     vus_max........................: 50      min=50           max=50


running (20.7s), 00/50 VUs, 576 complete and 0 interrupted iterations
default ✓ [======================================] 00/50 VUs  20s
ERRO[0021] thresholds on metrics 'http_req_duration' have been crossed 
```

다음은 `k6` 테스트 데이터 각 항목의 의미를 설명하는 표입니다.

| **Metric**                   | **설명**                                                   | **단위**            | **예시**                                 |
| ---------------------------- | ---------------------------------------------------------- | ------------------- | ---------------------------------------- |
| **execution**                | 실행 환경 (로컬 실행, 클라우드 등)                         | 텍스트              | `local`                                  |
| **script**                   | 실행된 스크립트 이름                                       | 텍스트              | `sample.js`                              |
| **output**                   | 테스트 결과 출력 대상                                      | 텍스트              | `-` (콘솔 출력)                          |
| **scenarios**                | 시나리오 개수와 가상 사용자(VU) 및 실행 단계, 지속 시간 등 | 텍스트              | 1 시나리오, 50 VUs, 3단계 50초 최대 시간 |
| **checks**                   | 검증 성공 비율 (성공/전체)                                 | 백분율              | `100.00%` (1152 중 1152 성공)            |
| **data_received**            | 테스트 중 받은 데이터의 총량                               | 데이터 크기 (MB/KB) | `2.0 MB`                                 |
| **data_sent**                | 테스트 중 보낸 데이터의 총량                               | 데이터 크기 (MB/KB) | `75 kB`                                  |
| **http_req_blocked**         | 요청이 대기(block)한 시간                                  | 시간 (ms/µs)        | 평균: `6.91ms`, 최대: `459.37ms`         |
| **http_req_connecting**      | 서버 연결을 시도한 시간                                    | 시간 (ms/µs)        | 평균: `3.11ms`, 최대: `257.49ms`         |
| **http_req_duration**        | HTTP 요청 전체 소요 시간                                   | 시간 (ms)           | 평균: `178.21ms`, 최대: `509.27ms`       |
| **http_req_failed**          | 실패한 요청 비율 (0 = 모든 요청 성공)                      | 백분율              | `0.00%`                                  |
| **http_req_receiving**       | 서버 응답 데이터를 수신하는 데 걸린 시간                   | 시간 (ms/µs)        | 평균: `517µs`, 최대: `25.79ms`           |
| **http_req_sending**         | 요청 데이터를 보내는 데 걸린 시간                          | 시간 (ms/µs)        | 평균: `145.89µs`, 최대: `5.47ms`         |
| **http_req_tls_handshaking** | TLS 핸드셰이크에 걸린 시간                                 | 시간 (ms)           | 평균: `3.65ms`, 최대: `319.59ms`         |
| **http_req_waiting**         | 요청이 완료되기까지 대기한 시간 (TTFB, Time To First Byte) | 시간 (ms)           | 평균: `177.55ms`, 최대: `508.46ms`       |
| **http_reqs**                | 수행된 HTTP 요청의 총 개수                                 | 요청 수 (count)     | `1152`                                   |
| **http1_req_duration**       | HTTP/1.1 요청의 평균 소요 시간                             | 시간 (ms)           | 평균: `189.29ms`                         |
| **http2_req_duration**       | HTTP/2 요청의 평균 소요 시간                               | 시간 (ms)           | 평균: `167.13ms`                         |
| **iteration_duration**       | 각 테스트 반복(iteration)당 소요 시간                      | 시간 (s)            | 평균: `1.37s`                            |
| **iterations**               | 완료된 반복(iteration)의 총 수                             | 반복 수 (count)     | `576`                                    |
| **vus**                      | 현재 활성 상태인 가상 사용자(VU)의 수                      | 사용자 수 (count)   | 최소: `7`, 최대: `50`                    |
| **vus_max**                  | 테스트 중 가상 사용자의 최대 수                            | 사용자 수 (count)   | `50`                                     |

------

### 요약:

- **HTTP 요청 시간**:
  - `http_req_duration`: 요청-응답의 전체 소요 시간.
  - `http_req_waiting`: 서버로부터 첫 번째 바이트(TTFB)를 받을 때까지 대기한 시간.
  - `http_req_receiving`: 서버 응답 데이터를 수신하는 데 걸린 시간.
- **성공률**:
  - `checks`: 모든 검증이 성공적으로 완료되었는지 비율로 확인.
  - `http_req_failed`: 실패한 HTTP 요청 비율.
- **데이터**:
  - `data_received`: 서버에서 수신한 데이터 총량.
  - `data_sent`: 서버로 전송한 데이터 총량.



# 평균 부하 테스트: 초보자를 위한 가이드

* https://grafana.com/blog/2024/01/30/average-load-testing/

------

### **평균 부하 테스트란?**

평균 부하 테스트는 시스템이 일반적인 부하(일상적인 사용 시나리오)에서 어떻게 성능을 발휘하는지를 평가하는 부하 테스트 유형입니다. 이는 프로덕션 환경에서의 평상시 부하를 시뮬레이션하며, 동시 사용자 수와 초당 요청 수를 반영하여 평균적인 행동을 재현합니다.

평균 부하 테스트는 다음과 같은 방식으로 진행됩니다:

- 테스트 초기에는 트래픽(Throughput) 또는 가상 사용자(VU)를 점진적으로 증가시킵니다.
- 평균 부하를 일정 기간 동안 유지합니다.
- 테스트 종료 시 갑작스럽게 멈추거나 부하를 점진적으로 줄이는 램프다운(ramp-down) 단계를 가질 수 있습니다.

**참고**: 부하 테스트라는 용어는 모든 유형의 부하 테스트를 포함할 수 있으므로, 혼동을 피하기 위해 본 가이드에서는 평균 부하 테스트(average-load test)라는 명칭을 사용합니다. 때로는 이를 "하루 동안의 테스트(day-in-life test)" 또는 "볼륨 테스트(volume test)"라고 부르기도 합니다.

------

### **언제 평균 부하 테스트를 실행해야 할까?**

평균 부하 테스트는 시스템이 **일상적인 부하**에서 성능 목표를 충족하는지 이해하는 데 도움을 줍니다. 여기서 일상적인 부하란, 평균 사용자 수가 동시에 애플리케이션에 액세스하여 일반적인 작업을 수행하는 상황을 의미합니다.

평균 부하 테스트를 실행해야 하는 경우:

- **평균 부하에서 시스템 성능 평가**.
- **램프업(ramp-up) 또는 전체 부하 기간 동안 초기 성능 저하 징후 식별**.
- **코드나 인프라 변경 이후에도 시스템이 성능 표준을 충족하는지 보장**.

------

### **평균 부하 테스트 준비 시 고려 사항**

1. **사용자 수와 시스템 프로세스당 일반적인 처리량 파악**:
   - APM(애플리케이션 성능 관리 도구)이나 프로덕션 환경의 분석 도구를 사용해 데이터를 수집하세요.
   - 해당 도구를 사용할 수 없다면, 비즈니스에서 제공하는 추정치를 활용합니다.
2. **부하를 점진적으로 증가**:
   - 램프업(ramp-up) 기간을 설정해 트래픽을 목표 부하로 천천히 증가시킵니다.
   - 일반적으로 램프업 기간은 총 테스트 시간의 5~15% 정도로 설정합니다.
     - 시스템을 준비하거나 자동 확장(auto-scaling)할 시간을 제공합니다.
     - 낮은 부하와 평균 부하 간의 응답 시간 차이를 비교할 수 있습니다.
     - 클라우드 서비스(Grafana Cloud k6 등)를 사용할 경우, 자동화된 성능 경고가 시스템의 예상 동작을 이해하도록 돕습니다.
3. **램프업 기간보다 더 긴 평균 부하 유지**:
   - 평균 부하를 일정 기간 유지하여 성능 추세를 평가하세요. 일반적으로 램프업 시간의 5배 이상 설정하는 것이 좋습니다.
4. **램프다운(ramp-down) 고려**:
   - 가상 사용자(VU) 활동을 점진적으로 감소시키는 램프다운 단계를 추가합니다. 램프다운 기간은 보통 램프업 시간과 같거나 약간 짧게 설정합니다.

------

### **평균 부하 테스트 예제**

**중요**: 부하 테스트를 처음 실행하는 경우, 작은 규모로 시작하거나 램프업 속도를 느리게 설정하세요. 예상보다 애플리케이션이나 인프라가 약할 수 있습니다. 실제로 많은 사용자가 부하 테스트를 실행하다가 애플리케이션(또는 스테이징 환경)이 빠르게 크래시 되는 사례가 있었습니다.

평균 부하 테스트의 목표:

1. 스크립트의 활동을 증가시켜 원하는 사용자 수와 처리량에 도달.
2. 그 부하를 일정 시간 유지.
3. 테스트 종료 시, 갑작스럽게 중단하거나 점진적으로 부하를 줄임.

------

### **Grafana k6로 평균 부하 테스트 설정**

```javascript
import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  stages: [
    { duration: '5m', target: 100 }, // 5분 동안 사용자 수를 1명에서 100명으로 증가
    { duration: '30m', target: 100 }, // 30분 동안 100명 유지
    { duration: '5m', target: 0 },   // 5분 동안 사용자 수를 0명으로 감소
  ],
};

export default () => {
  const urlRes = http.get('https://test-api.k6.io');
  sleep(1);
};
```

**설명**:

- **램프업**: 초기 사용자 수에서 100명으로 점진적으로 증가.
- **평균 부하 유지**: 100명의 사용자가 일정 시간 동안 부하를 유지.
- **램프다운**: 사용자 수를 서서히 감소시키며 테스트 종료.

------

### **결과 분석**

1. **램프업 기간 중 성능 저하 여부 확인**:
   - 응답 시간이 부하 증가와 함께 감소하는지 확인합니다. 일부 시스템은 램프업 동안에도 장애를 일으킬 수 있습니다.
2. **전체 부하 기간 동안 성능 및 리소스 소비 안정성 검증**:
   - 시스템이 전반적으로 안정적인지, 불규칙적인 동작을 나타내는지 평가합니다.
3. **추가적인 스트레스 테스트 준비**:
   - 평균 부하 테스트를 성공적으로 마쳤다면, 더 높은 부하 조건에서 시스템이 어떻게 동작하는지 확인하기 위해 스트레스 테스트를 실행할 수 있습니다.



# K6 custom metrics

https://grafana.com/docs/k6/latest/using-k6/metrics/create-custom-metrics/

* https://grafana.com/docs/k6/latest/javascript-api/k6-http/response/

```js
import http from 'k6/http';
import { sleep } from 'k6';
import { Counter } from 'k6/metrics';

export const options = {
    vus: 5,
    duration: '5s',
    thresholds: {
        http_req_duration: ['p(95)<250'],
        my_counter: ['count>10']
    }
}

let myCounter = new Counter('my_counter');

export default function () {
    const res = http.get('https://test.k6.io/');
    myCounter.add(1);
    sleep(2);
}
```



# K6 Test Lifecycle

https://grafana.com/docs/k6/latest/using-k6/test-lifecycle/



# k6 Enviromnent variables 환경변수 사용

* https://grafana.com/docs/k6/latest/using-k6/environment-variables/

```js
import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
  const res = http.get(`http://${__ENV.MY_HOSTNAME}/`);
  sleep(1);
}
```

```
$ k6 run -e MY_HOSTNAME=test.k6.io script.js
```



# 다양한 랜덤 아이템 유틸

* https://grafana.com/docs/k6/latest/javascript-api/jslib/utils/randomitem/



# CSV 또는 JSON 파일로부터 읽어오기

* https://grafana.com/docs/k6/latest/examples/data-parameterization/#from-a-json-file



# 그라파나 K6 클라우드

1. 계정 가입 - https://grafana.com/products/cloud/k6/



# K6 웹 대쉬보드

* https://grafana.com/docs/k6/v0.52.x/results-output/web-dashboard/

### k6로 부하 테스트 실행

script.js 파일이 있는 디렉토리에서 아래의 명령어를 실행하여 부하 테스트을 수행한다.

| `1 2 3 4 5 6 7 8 9 ` | `docker run \ --rm \ --add-host host.docker.internal:host-gateway \ -v ./:/k6-dir \ -p 5665:5665 \ -e K6_WEB_DASHBOARD=true \ -e K6_WEB_DASHBOARD_EXPORT=/k6-dir/report.html \ -e K6_WEB_DASHBOARD_PERIOD=1s \ grafana/k6:0.52.0 run /k6-dir/script.js ` |
| -------------------- | ------------------------------------------------------------ |
|                      |                                                              |

Copy

| 옵션                                           | 설명                                                         |
| :--------------------------------------------- | :----------------------------------------------------------- |
| –rm                                            | 부하테스트 종료 후 컨테이너를 삭제                           |
| –add-host host.docker.internal:host-gateway    | container 내부에서 docker가 실행되는 호스트에 접근 가능하도록 설정 |
| -v ./:/k6-dir                                  | 명령어 실행 폴더를 container에 bind                          |
| -p 5665:5665                                   | Web Dashboard 접속을 위해 container의 5665포트를 host의 5665포트로 bind |
| -e K6_WEB_DASHBOARD=true                       | Web Dashboard 기능을 활성화                                  |
| -e K6_WEB_DASHBOARD_EXPORT=/k6-dir/report.html | 부하 테스트 종료 후 Test report 작성 위치 설정               |
| -e K6_WEB_DASHBOARD_PERIOD=1s                  | 부하 테스트 시, Test report용 데이터를 집계하는 주기 설정 (default 10s) |

### Web Dashboard

부하 테스트 중에 [http://localhost:5665](http://localhost:5665/)로 접속하면 테스트 결과를 바로바로 Dashboard를 통해 확인이 가능하다. Dashboard에서 확인 가능한 정보는 전부 Test report에서도 확인 가능하다.

- 주의!!
  - 해당 페이지를 끄지 않으면 k6 테스트도 종료되지 않는다.
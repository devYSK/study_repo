# metrics endpoint 

actuator 의 수많은 endpoint 중 가장 중요한 endpoint 중 하나가 metrics endpoint 입니다.

회사에서 운영/모니터링시 주로 사용하는게 cpu, mem, disk usage, thread count, cache 용량 등인데 이런 정보는 대부분 metrics endpoint 에서 제공되기 때문입니다. 

application.yml 에 아래처럼 web 에 모두 노출시키도록 설정 한 후 

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

 

/actuator/metrics 으로 들어가면 아래처럼 다양한 metric 정보들이 나와 있으며, 

* http://localhost:8080/actuator/metrics

```json
// 20230528222532
// http://localhost:8080/actuator/metrics

{
  "names": [
    "application.ready.time",
    "application.started.time",
    "disk.free",
    "disk.total",
    "executor.active",
    "executor.completed",
    "executor.pool.core",
    "executor.pool.max",
    "executor.pool.size",
    "executor.queue.remaining",
    "executor.queued",
    "hikaricp.connections",
    "hikaricp.connections.acquire",
    ....
```

나와 있는 metric 이름중 하나를 선택해서 url path 에 넣어주면 해당 이름의 자세한 metric 정보를 알 수 있습니다.

ex ) - http://localhost:8080/actuator/metrics/disk.free

```json
// http://localhost:8080/actuator/metrics/disk.free

{
  "name": "disk.free",
  "description": "Usable space for path",
  "baseUnit": "bytes",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 633867198464
    }
  ],
  "availableTags": [
    {
      "tag": "path",
      "values": [
        "/Users/ysk/study/study_repo/inf-spring-actuator/actuator/."
      ]
    }
  ]
}
```

spring boot 에서는 위와 같이 다양한 metrics 정보를 actuator 를 통해 수집 및 정보를 제공하고 있습니다. 

아주 많은 종류가 있기에 중요한 것만 설명을 하겠으며 보다 자세한 내용은 아래 링크를 참고해주세요

https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.supported

# 외부 모니터링 시스템과의 연동

actuator 가 cpu, disk, http 등의 metric 정보를 수집해서 web endpoint 로 제공을 하는데, 결국 어딘가에서 이 정보를 읽어서 통합 모니터링 하는데 사용하려는게 목적일 겁니다.

**datadog**, **elastic**, **prometheus**, **stackdriver**, **dynatrace**, influx **와** 같은 모니터링 연관 시스템에서 metric 정보를 읽어서 해당 시스템 스토리지에 저장하고 grafana와 같은 GUI툴을 통해 시간대별로 metric 값의 변화를 차트로 보여주면 좋을듯 합니다. 

mysql, oracle 등 다양한 RDB가 있고 모두 ansi SQL 이라는 표준 SQL을 지원하나, 각자 다른 SQL 문법도 존재하는걸 알 고 있을겁니다. 모니터링 시스템들도 각자만의 프로토콜로 구현되어 있습니다.

- datadog 에는 json 형태로 metric 정보를 전달해야 하고,
- elastic 에는 xml 형태로 전달해야 하고,
- prometheus 에는 json, xml 도 아닌 proemtheus 자체 포맷으로 전달해야 하고,
- 또 다른 시스템에는 tcp로 데이터를 push해야 할수도 있고,
- 또 다른 어떤 시스템에는 polling 해야 할수도 있고...

actuator 는 metric 부분에 대해서는 micrometer 를 핵심 라이브러리로 사용하므로 정확히는 micrometer 가 이 역할을 수행한다고 봐도 됩니다

지원되는 외부 모니터링 시스템관련 상세내용은 아래 확인 

* https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.export

만약 elastic 모니터링 시스템과 연동되어야 한다면 application.yml 에 아래처럼 적어주면 metric 정보를 해당 host로 전달해줍니다. 

* https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.export.elastic

```yml
management:
  elastic:
    metrics:
      export:
        host: "https://elastic.example.com:8086"
```

Prometheus 같은 경우  json, xml 도 아닌 proemtheus 자체 포맷으로 전달해야 하고,

* https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.export.prometheus

```yaml
scrape_configs:
  - job_name: "spring"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["HOST:PORT"]
```

아래 가이드처럼 prometheus 가 설치된 곳의 prometheus.yml 에 spring boot 의 ip, port, path 정보를 적어줘야 합니다. 



# MetricsEndpoint

metric endpoint는 MetricsEndpoint클래스가 담당합니다. 

@ReadOperation 어노테이션이 붙은 2개 메서드가 있습니다.

*  /actuator/metrics path 에 대한 메서드
*  /actuator/metrics/{name} 형식의 path 에 대한 메서드

```java
package org.springframework.boot.actuate.metrics;

@Endpoint(id = "metrics")
public class MetricsEndpoint {

	private final MeterRegistry registry; // here

	@ReadOperation // here 
	public MetricNamesDescriptor listNames() {
		Set<String> names = new TreeSet<>();
		collectNames(names, this.registry);
		return new MetricNamesDescriptor(names);
	}

	@ReadOperation // here 
	public MetricDescriptor metric(@Selector String requiredMetricName, @Nullable List<String> tag) {
		List<Tag> tags = parseTags(tag);
		Collection<Meter> meters = findFirstMatchingMeters(this.registry, requiredMetricName, tags);
		if (meters.isEmpty()) {
			return null;
		}
		Map<Statistic, Double> samples = getSamples(meters);
		Map<String, Set<String>> availableTags = getAvailableTags(meters);
		tags.forEach((t) -> availableTags.remove(t.getKey()));
		Meter.Id meterId = meters.iterator().next().getId();
		return new MetricDescriptor(requiredMetricName, meterId.getDescription(), meterId.getBaseUnit(),
				asList(samples, Sample::new), asList(availableTags, AvailableTag::new));
	}

```



MeterRegistry 가 핵심 클래스로 보이는데, 이게 spring 에 속한게 아니라 아래처럼 io.micrometer 에 속한 클래스입니다. 즉 actuator 는 micrometer 가 핵심이고 spring boot은 micrometer 를 spring 에서 사용하기 쉽게 자동 구성 등을 해주는 역할을 하는겁니다.



## custom metrics 

spring boot 에서는 MeterRegistry 에 meter 를 등록하려면 MeterBinder 를 bean 으로 등록하라고 아래처럼 권고하고 있습니다.

* https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.registering-custom



재고 관리 시스템용 spring boot 라고 가정을 했을때, 재고 수량은 늘었다 줄었다 할겁니다. 

이걸 metics 에 등록해보도록 하겠습니다. 

 

우선 재고 수량을 리턴해줄수 있는 bean 을 하나 만들어봤습니다.

테스트 용도이니 현재시간을 long 으로 변환한 값을 재고수량이라고 간주합시다. 

```java
@Component
public class MyStockManager {

	public long getStock() {
		// 재고수량을 리턴해야 하지만, 테스트용도이니 현재시간의 long 값을 재고수량으로 대체함.
		return System.currentTimeMillis();
	}
}
```

다음 Configuration 작성

```java
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class MyStockMeterBinderConfiguration {
	
	@Bean
	public MeterBinder myStockMeterBinder(MyStockManager myStockManager) {
		return registry -> 
			Gauge.builder("myStockCount",  // name 지정 
				myStockManager, 
				MyStockManager::getStock)
				.register(registry);
	}
}
```

http://localhost:8080/actuator/metrics/myStockCount 호출

* Gauge.builder 에 지정한 name 입니다.

```java
// http://localhost:8080/actuator/metrics/myStockCount

{
  "name": "myStockCount",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 1685281115319
    }
  ],
  "availableTags": [
    
  ]
}
```



# Micrometer

MeterRegistry, MeterBinder 등 핵심 클래스가 모두 spring 이 아닌 micrometer 의 것입니다. 그래서 이제부터는 spring 공식 가이드가 아닌 micrometer 공식 가이드를 참고하여 metric 관련 상세 정보를 살펴보겠습니다.

*  [https://micrometer.io](https://micrometer.io/)

* https://micrometer.io/docs/concepts



## Counter

Counter 는 이름 그대로 횟수를 세어 metric 으로 제공합니다. 횟수이므로 1, 2, 100, 3000 처럼 자연수만 가능하지 1.3 처럼 소수나 -100 처럼 음수는 불가능합니다.

일반적으로 cache hit 에 대한 누적 counter, http request 누적 횟수 counter 와 같이 지금까지 특정 이벤트가 몇번 발생했는지를 누적값으로 제공할때 Counter 를 사용하면 됩니다.

 

공식가이드( https://micrometer.io/docs/concepts#_counters )에는 아래처럼 Counter builder 를 이용해서 값을 세팅한 후 MeterRegistry 에 등록하면 Counter 가 만들어진다고 적혀있습니다.

```java
Counter counter = Counter
    .builder("counter")
    .baseUnit("beans") // optional
    .description("a description of what this counter does") // optional
    .tags("region", "test") // optional
    .register(registry);
```

Rest controller 를 하나 만들고 http request 가 올때마다 count 를 하나씩 증가시키는 Counter 를 만들어보겠습니다. 

 

### Counter.builder() 사용

counter 관리하는 manager 를 bean 으로 등록시키고 , 다른 bean 에서 counter 증가시키는 메서드를 호출하도록 구현했습니다.

MeterRegistry는 spring boot 에서 기본 생성해주기에 생성자 주입으로 bean을 주입했습니다.

counter 의 필수필드만으로 httpRequestCounter 를 생성하고 meterRegistry에 등록했습니다



Manager 생성

```java
@Service
@RequiredArgsConstructor
public class MyHttpRequestManager {

	private final MeterRegistry meterRegistry;  // 생성자 주입

	private Counter httpRequestCounter;  //  아래 init() 메서드에서 객체 생성 후 대입해줌

	/**
	 * registry 에 등록
	 */
	@PostConstruct
	void init() {
		httpRequestCounter = Counter.builder("myHttpRequest") // 이름 등록 
			.register(meterRegistry);
	}

	 // counter 증가시킬 필요가 있을때 외부에서 이 메서드를 호출
	public void increase() {
		httpRequestCounter.increment();
	}

}
```

controller 생성

```java
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class MetricsController {

    private final MyHttpRequestManager myHttpRequestManager;  // 생성자 주입

    @GetMapping("/req")
    public String request() {
        myHttpRequestManager.increase();  // counter 를 증가시킴
        return "ok";
    }

}
```

* Counter.builder에서 이름 지정

접속 - http://localhost:8080/actuator/metrics/myHttpRequest

```json
// 20230528225421
// http://localhost:8080/actuator/metrics/myHttpRequest

{
  "name": "myHttpRequest",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 0.0 // 지금은 0 
    }
  ],
  "availableTags": [
    
  ]
}
```

api 호출을 통하여 증가시키기 - http://localhost:8080/api/req

다시 http://localhost:8080/actuator/metrics/myHttpRequest 호출

```json
// http://localhost:8080/actuator/metrics/myHttpRequest

{
  "name": "myHttpRequest",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 1.0 // 1로 증가 
    }
  ],
  "availableTags": [
    
  ]
}
```

* counter 값이증가한것을 확인. 

그러나 위 코드의 문제점은 Counter 값을 증가시키기 위해 application 내의 비지니스 로직 사이에 counter 증가시키는 메서드를 명시적으로 호출해야 하는 부분.



## FunctionCounter.builder() 사용

micrometer 에서는 위 문제를 해결하기 위해 FunctionCounter 라는 걸 제공합니다.

( https://micrometer.io/docs/concepts#_function_tracking_counters )

```java
MyCounterState state = ...;   // 횟수관리하는 클래스라고 가정.

FunctionCounter counter = FunctionCounter
    .builder("counter", state, state -> state.count())  // count 값을 특정 함수 호출하고 리턴값을 통해 받아오도록 함
    .register(registry);
```

builder 메서드의 

2번째값으로 위 객체를 전달하고, 

3번째 값으로 해당 객체의 어떤 메서드를 호출하면 되는지를 람다식으로 넣어주면 됩니다. 

이를 통해 횟수 관리는 app 자체적으로 하고, metrics 노출만 micrometer 와 연동되도록 할 수 있습니다.

```java
@Service
public class MyHttpRequestManagerWithoutMicrometer {

	private AtomicLong count = new AtomicLong(0);

	public long getCount() {
		return count.get() + System.currentTimeMillis(); // 값이 변경되는걸 보기 위해 현재시간을 추가함. 원칙적으로는 filter 등을 통해 count값이 변경되어야 함.
	}

	// 아래 메서드는 filter 나 interceptor 등을 통해 http 요청시마다 호출되도록 구현했다고 가정.
	public void increase() {
		count.incrementAndGet();
	}
}
```

micrometer와 아무 연관성이 없는 횟수 관리 bean을 생성합니다. 

http request 횟수를 관리하는 bean 이므로 실제 count 증가는 **filter** 나 **interceptor** 에서 아래 bean의 increase() 메서드를 호출하도록 하면 됩니다

*  예제를 단순하게 하기 위해 filter, interceptor 연동은 생략

config 클래스 생성

```java
@Configuration
@RequiredArgsConstructor
public class MyFunctionCounterConfig {

	private final MyHttpRequestManagerWithoutMicrometer myManager;

	private final MeterRegistry meterRegistry;

	@PostConstruct
	void init() {
		FunctionCounter.builder("myHttpRequestWithoutMicrometer", // name
				myManager, MyHttpRequestManagerWithoutMicrometer::getCount)
			.register(meterRegistry);
	}
}
```

FunctionCounter 를 이용해서 counter 를 등록하면서 위 bean을 파라미터로 넣어줍니다

counter 값이 필요할때마다 파라미터로 넘긴 bean의 메서드를 호출해서 count를 가져오게 됩니다. 



http://localhost:8080/actuator/metrics/myHttpRequestWithoutMicrometer 호출

```json
// http://localhost:8080/actuator/metrics/myHttpRequestWithoutMicrometer

{
  "name": "myHttpRequestWithoutMicrometer",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 1685282654055
    }
  ],
  "availableTags": [
    
  ]
}
```

spring boot 소스에서도 위와 같이 FunctionCounter 를 이용한 예제를 쉽게 찾을 수 있습니다.

아래처럼 Counter.builder 라고 검색을 하면 spring boot 자체 소스 혹은 사용하는 라이브러리중 해당 코드가 있는걸 쉽게 찾을 수 있으며, 위 예제처럼 bean과 bean을 파라미터로 넘겨주는 람다식을 builder() 메서드의 파라미터로 넘겨주고 있습니다.

## MeterBinder

FunctionCounter 를 meterRegistry에 등록하는 방법으로 MeterBinder 를 이용할수도 있습니다.

MeterBinder 가 **인터페이스** 타입이라서 **bindTo**() 메서드안에 **FunctionCounter** 를 사용하면 됩니다. 

counter 등록 방법이 여러가지가 있으므로 자주 쓸만한 기능을 모두 알아두어야 다른 사람코드를 분석하는데 도움이 될 수있습니다. 

```java
@Configuration
public class CounterConfigWithMeterBinder {

	@Bean
	public MeterBinder myTimerWithMeterBinder(MyHttpRequestManagerWithoutMicrometer manager) {
		return registry -> FunctionCounter.builder("myHttpRequestWithMeterBinder",
				manager,
				MyHttpRequestManagerWithoutMicrometer::getCount)
			.register(registry);
	}
}
```

## @Counted 사용

counter 를 가만히 생각해보면  특정 메서드 호출될때마다 counter 값을 하나씩 증가시켜 주는 식으로 구현해도 됩니다.

 아래 문서처럼 register()는 새로운걸 등록하거나, 이미 존재하는걸 리턴해줍니다. 즉 동일한 이름의 counter 로 register()를 호출하면, 두번째부터는 등록이 아니라 등록된걸 리턴해줍니다. 

따라서 중복 등록에 대해 걱정할 필요 없습니다. 

이 부분은 추후 다룰 Gauge, Timer 도 동일하게 적용됩니다.



spring에 @Transactional 이나 @Cacheable 과 같은 어노테이션을 적어주면 공통 코드가 각 메서드에 들어가게 되듯이 micrometer 에서도 이런 어노테이션을 제공해주고 있습니다. 

**그게 바로 @Counted 입니다.**

spring AOP 를 이용하므로 아래처럼 aop 의존성을 넣어줍니다. 정확히는 aspectj 의존성이 필요한데, spring aop 의존성을 넣으면 함께 포함되기에 아래 의존성만으로 충분합니다.

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

gradle

```groovy

```





controller

```java
import io.micrometer.core.annotation.Counted;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MetricsController {

	private final MyHttpRequestManager myHttpRequestManager;  // 생성자 주입

	@Counted(value = "myCountedAnnotationCount")
	@GetMapping("/req")
	public String request() {
		// myHttpRequestManager.increase();  // counter 를 증가시킴
		return "ok";
	}
}
```

CountedAspect 클래스를 bean 으로 등록해줍니다. 

```java
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class CountConfig {

	@Bean
	public CountedAspect countedAspect(MeterRegistry meterRegistry) {
		return new CountedAspect(meterRegistry);
	}
}
```



http://localhost:8080/api/req 를 호출

```json
ok
```

다음 http://localhost:8080/actuator/metrics/myCountedAnnotationCount 호출

```json
// http://localhost:8080/actuator/metrics/myCountedAnnotationCount

{
  "name": "myCountedAnnotationCount",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 1.0
    }
  ],
  "availableTags": [
    {
      "tag": "result",
      "values": [
        "success"
      ]
    },
    {
      "tag": "exception",
      "values": [
        "none"
      ]
    },
    {
      "tag": "method",
      "values": [
        "request"
      ]
    },
    {
      "tag": "class",
      "values": [
        "com.ys.actuator.httpcounter.MetricsController"
      ]
    }
  ]
}
```

@Counted 는 최소 1번은 실행되어야 meterRegistry 에 등록되기에 위 **rest api를 1번 이상 호출** 한 후 

다시 http://127.0.0.1:8080/actuator/metrics 에서 찾아보면 값이 보입니다.



# metrics tags

웹브라우저에서 아래 링크로 들어가봅시다. 

http://127.0.0.1:8080/actuator/metrics/http.server.requests

availableTags 라는 필드가 보이고 이걸 보니 hash tag 같은 느낌이 듭니다. 

즉 필터링해서 특정 케이스에 대한 것만 볼 수 있을것 같습니다. 

```json
// http://127.0.0.1:8080/actuator/metrics/http.server.requests

{
  "name": "http.server.requests",
  "baseUnit": "seconds",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 3.0
    },
    {
      "statistic": "TOTAL_TIME",
      "value": 0.129526333
    },
    {
      "statistic": "MAX",
      "value": 0.0
    }
  ],
  "availableTags": [
    {
      "tag": "exception",
      "values": [
        "none"
      ]
    },
    {
      "tag": "method",
      "values": [
        "GET"
      ]
    },
    {
      "tag": "error",
      "values": [
        "none"
      ]
    },
    ... 생략 
```

* https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.metrics.endpoint

위와 같이 쿼리 스트링에 tag=KEY:VALUE 형식으로 넣으라고 가이드 되어 있습니다. 

만약 여러값을 필터링 하고 싶으면 tag=KEY1:VALUE1&tag=KEY2:VALUE2 처럼 이어서 넣으면 됩니다. 

* http://localhost:8080/actuator/metrics/jvm.memory.max?tag=area:heap

```json
// http://localhost:8080/actuator/metrics/jvm.memory.max?tag=area:heap

{
  "name": "jvm.memory.max",
  "description": "The maximum amount of memory in bytes that can be used for memory management",
  "baseUnit": "bytes",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 8589934590
    }
  ],
  "availableTags": [
    {
      "tag": "id",
      "values": [
        "G1 Old Gen",
        "G1 Survivor Space",
        "G1 Eden Space"
      ]
    }
  ]
}
```



# metrics 중 Gauge 

* 이전에 다룬 Counter 타입의 경우 이름 그대로 횟수값을 다루는데 주로 사용합니다

Gauge(게이지)는 자동차 속도 계기판과 같이 커졌다 작아졌다 할 수 있는 값에 사용됩니다

* 보통 cpu usage, mem usage , 
* 사용중인 thread count, 
* 사용중인 connection pool count 등에 사용됩니다

Gauge 의 경우, 조회시도 할때만 해당 값을 계산하면 됩니다.

반적으로 아래처럼 특정 metric 를 관리하는 객체를 파라미터로 전달하는 형식으로 등록하게 됩니다.

```java
Gauge gauge = Gauge
    .builder("mygauge", myObj, myObj::gaugeValue)  <-- 객체와 객체내의 메서드를 전달
    .register(registry);
```



ex) queue 에 대기중인 수를 리턴하는 service 라고 가정

* 이 값은 커졌다 작아졌다 할 수 있으니 Counter 가 아닌 Gauge 가 적절합니다.

```java
@Service
public class QueueManager {

	public long getQueueSize() {
		return System.currentTimeMillis();  // queue에 대기중인 데이터의 수를 리턴하는것으로 가정
	}
}
```

```java
@Configuration
@RequiredArgsConstructor
public class GaugeConfig {

	private final QueueManager queueManager;

	private final MeterRegistry meterRegistry;

	@PostConstruct
	public void register() {
		Gauge gauge = Gauge
			.builder("my.queue.size", queueManager,
				queueManager -> queueManager.getQueueSize())
			.register(meterRegistry);

	}
}
```

http://localhost:8080/actuator/metrics/my.queue.size 호출

```json
// http://localhost:8080/actuator/metrics/my.queue.size

{
  "name": "my.queue.size",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 1685288616293
    }
  ],
  "availableTags": [
    
  ]
}
```



###  MeterBinder with MeterBinder

좀 더 편리하게 사용하라고 MeterBinder 라는 인터페이스가 제공되며   MeterBinder 를 implement 한건 bean으로 등록하면 필요한 bean들은 자동으로 주입이 됩니다

```java
@Configuration
public class GaugeConfigWithMeterBinder {

	@Bean
	public MeterBinder queueSize(QueueManager queueManager) {
		return registry -> Gauge.builder("my.queue2.size",
			queueManager,
			QueueManager::getQueueSize)
			.register(registry);
	}
}
```



# Metric - Timer

시간을 측정하는 metric 입니다.

## timer 등록

다른 metric type과 동일하게 builder 를 이용해서 timer 를 만든 후 meterRegistry 에 등록해줍니다. 

my.timer 라고 파라미터값을 넣었으므로 actuator 에서는 my.timer 가 path에 들어가게 됩니다.

timer 의 경우 시간값을 측정하고 싶은 곳에 timer 로 감싸서 구현을 해야합니다. 

그래서 반드시 Timer 를 bean 으로 등록해둬야 합니다.

```java
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Configuration
@RequiredArgsConstructor
public class TimerConfig {

	private final MeterRegistry meterRegistry;

	@Bean
	public Timer myTimer() {
		return Timer
			.builder("my.timer")
			.register(meterRegistry);
	}
}
```

## timer 적용

아래처럼 controller 내의 특정 메서드의 수행 시간을 timer metric 을 이용해서 측정해봤습니다.

위에서 만든 Timer bean을 생성자 주입으로 받았습니다. 

@Qualifier("myTimer")는 넣지 않아도 오류가 나지 않으나, timer 를 개인적으로 더 추가해서 테스트 하는 분들이 있을듯 하여 Timer 타입의 bean이 2개 이상 있어도 오류가 나지 않게 미리 @Qualifier 를 추가해두었습니다.

timer 로 측정하고 싶은 코드를 Timer.record() 메서드 내에 넣어주면 됩니다.

```java
@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimerController {

	@Qualifier("myTimer")  // 없어도 오류는 없으나 Timer 타입 bean이 추가되면 오류나므로 예방차원에서 추가.
	private final Timer myTimer;

	@GetMapping("/timer")
	public String timer() {
		myTimer.record(() -> {
			try {
				// 3초 정도 걸리는 코드가 있다는 가정
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		return "ok";
	}

}
```

* 3초 정도 걸리는 코드가 있다는 가정

http://localhost:8080/api/timer 후 접속 후 

http://localhost:8080/actuator/metrics/my.timer에 접속

```json
// http://localhost:8080/actuator/metrics/my.timer
{
  "name": "my.timer",
  "baseUnit": "seconds",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 2.0
    },
    {
      "statistic": "TOTAL_TIME",
      "value": 6.006541208 // 2번 호출해서 총 6초 걸림.
    },
    {
      "statistic": "MAX",
      "value": 3.005719333
    }
  ],
  "availableTags": [
    
  ]
}
```



## timer debugging

timer 는 어떤식으로 동작하길래 Timer.record() 메서드의 파라미터에 Runnable 을 넣어줘야 할까요

record() 메서드에 디버그 포인트를 걸고 내부 코드를 따라가보니

Runnable 실행 전후에 시간을 알아낸후 그걸 계산해서 수행시간을 측정하는걸 알 수있습니다.

```java
public abstract class AbstractTimer extends AbstractMeter implements Timer {
 	// 생략
  @Override
  public void record(Runnable f) {
    final long s = clock.monotonicTime();
    
    try {
      f.run(); 
    }
    finally {
      final long e = clock.monotonicTime();
      record(e - s, TimeUnit.NANOSECONDS); 
    }  
  }

}
```

## Timer.Sample

좀 복잡한 로직에서 Timer 를 사용하고 싶다면 Timer.Sample 을 이용하는것도 좋은 방식입니다.

* https://micrometer.io/docs/concepts#_storing_start_state_in_timer_sample

```java
Timer.Sample sample = Timer.start(registry);

// do stuff
Response response = ...

sample.stop(registry.timer("my.timer", "response", response.status()));
```



my.timer2 에 대해 Timer.Sample 을 사용해봤습니다. 결과는 동일하니 테스트 결과는 skip 합니다.

```java
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimerController {

	private final MeterRegistry meterRegistry;

	@GetMapping("/timer2")
	public String timer2() {

		Timer.Sample sample = Timer.start(meterRegistry); // this 

		internal();

		sample.stop(meterRegistry.timer("my.timer2"));
		return "ok";
	}

	private void internal() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
```



## @Timed aop 어노테이션

micrometer 라이브러리에서는 AOP를 활용한 @Timed 라는 어노테이션을 제공하며 이를 통해 쉽게 timer 를 적용할 수 있습니다.

Timer metric 에서 @Timed 라는 어노테이션을 적용하려면 TimedAspect 타입의 bean을 아래처럼 등록해줘야 합니다

```java
@Configuration
public class TimedConfiguration {
	@Bean
	public TimedAspect timedAspect(MeterRegistry meterRegistry) {
		return new TimedAspect(meterRegistry);
	}
}
```

rest controller 에 아래처럼 특정 메서드 위에 @Timed 어노테이션을 사용했습니다. 

```java
import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/api")
public class TimerController {

	@Timed("my.timer3")
	@GetMapping("/timer3/{sleepSeconds}")
	public String timer3(@PathVariable("sleepSeconds") int sleepSeconds) throws InterruptedException {
		Thread.sleep(sleepSeconds);
		return "ok";
	}
}
```

* http://localhost:8080/api/timer3/1000
* http://localhost:8080/actuator/metrics/my.timer3

```java
// http://localhost:8080/actuator/metrics/my.timer3

{
  "name": "my.timer3",
  "baseUnit": "seconds",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 1.0
    },
    {
      "statistic": "TOTAL_TIME",
      "value": 1.007909
    },
    {
      "statistic": "MAX",
      "value": 1.007909
    }
  ],
  "availableTags": [
    {
      "tag": "exception",
      "values": [
        "none"
      ]
    },
    {
      "tag": "method",
      "values": [
        "timer3"
      ]
    },
    {
      "tag": "class",
      "values": [
        "com.ys.actuator.timer.TimerController"
      ]
    }
  ]
}
```

## FunctionTimer

Counter metric 파트에서 자체 counter 관리 bean 이 있다면, 이를 재활용하기 위해 FunctionCounter 라는 클래스를 이용하면 된다고 배웠습니다

timer 도 동일하게 FunctionTimer 가 있으며 사용 패턴은 동일합니다.



자체적으로 시간을 관리하는 bean 이 있다고 가정하겠습니다.

 테스트 용도이니, 현재 시간값을 count, 누적 시간 으로 지정했습니다. 

실무에서는 특정 메서드 호출횟수와 소요시간 등을 직접 계산하는 로직이 들어가야 합니다.

```java
@Service
public class MyTimerManager {

	public long getCount() {
		return System.currentTimeMillis();
	}

	public long getTotalTime() {
		return System.currentTimeMillis() * 2;
	}
}
```

timer 를 등록하기 위해 아래처럼 FunctionTimer 타입의 bean을 생성하며, 이때 위에서 만든 bean을 주입받아서 사용하면 됩니다.

```java
@Configuration
public class FunctionTimerConfig {

	@Bean
	FunctionTimer myFunctionTimer(MeterRegistry meterRegistry, MyTimerManager myTimerManager) {
		return FunctionTimer.builder("my.timer5.latency", 
				myTimerManager,
				MyTimerManager::getCount,
				MyTimerManager::getTotalTime,
				TimeUnit.SECONDS)
			.register(meterRegistry);
	}
}
```

* 수와 누적시간을 가져오는 메서드를 지정해주고, 누적시간값의 단위 즉 초, 분, 시 등을 지정해주면 됩니다. 
* 아쉽게도 FunctionTimer 의 경우 max 값은 지원되지 않는 것으로 보입니다.

## MeterBinder

MeterBinder 를 통해 등록도 가능합니다. 

MeterBinder 가 bean으로 등록되다보니 그에 맞게 bindTo() 메서드 문법에 맞게 구현해야 하는 차이가 있을 뿐 동작은 동일합니다.

```java
@Configuration
public class TimerConfigWithMeterBinder {

	@Bean
	public MeterBinder myTimerWithMeterBinder(MyTimerManager myTimerManager) {
		return registry -> {
			FunctionTimer functionTimer = FunctionTimer.builder("my.timerWithMeterBinder.latency",
					myTimerManager,
					MyTimerManager::getCount,
					MyTimerManager::getTotalTime,
					TimeUnit.SECONDS)
				.register(registry);
		};
	}
}
```
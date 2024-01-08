# 인프런 선착순 이벤트 시스템

* https://www.inflearn.com/course/lecture?courseSlug=%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%8B%A4%EC%8A%B5&unitId=152660&tab=curriculum



발생할 수 있는 문제점

- ﻿﻿쿠폰이 100개보다 많이 발급됐어요
- ﻿﻿이벤트페이지 접속이 안돼요
- ﻿﻿이벤트랑 전혀상관없는 페이지들도 느려졌어요.

문제 해결

- ﻿﻿트래픽이 몰렸을 때 대처할 수 있는 방법을 배웁니다.
- ﻿﻿redis 를 활용하여 쿠폰발급개수를 보장합니다.
- ﻿﻿kafka 를 활용하여 다른페이지들에 대한 영향도를 줄입니다.



쿠폰보다는 선착순 도메인 시스템에 관련됌



* Spring, Redis, Kafka



# 요구사항

선착순 100명에게 할인쿠폰을 제공하는 이벤트를 진행하고자 한다. 

이 이벤트는 아래와 같은 조건을 만족하여야 한다.

- 선착순 100명에게만 지급되어야한다. 

- 101개 이상이 지급되면 안된다. 
- 순간적으로 몰리는 트래픽을 버틸 수 있어야합니다.

# 환경세팅

mysol 명령어

```mysql
mysql -u root -p

create database coupon_example;

use coupon_example;
```

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/coupon_example
    username: root
    password: root
```



# 레디스 세팅

```
 docker run --name couponredis -d -p 6379:6379 redis
```



# 카프카 세팅

docker-compose.yml

```yaml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

```groovy
implementation 'org.springframework.kafka:spring-kafka'
```

카프카 실행

```
docker-compose up -d
```

카프카 실행종료

```yaml
docker-compose down
```

> 카프카란 ?

분산 이벤트 스트리밍 플랫폼

이벤트 스트리밍이란 소스에서 목적지까지 이벤트를 실시간으로 스트리밍 하는 것

토픽생성

```yaml
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic testTopic
```

프로듀서 실행

```yaml
docker exec -it kafka kafka-console-producer.sh --topic testTopic --broker-list 0.0.0.0:9092
```

컨슈머 실행

```yaml
docker exec -it kafka kafka-console-consumer.sh --topic testTopic --bootstrap-server localhost:9092
```



# 카프카 설정 

```java
@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, Long> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, Long> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}

```

```java
@Repository
@RequiredArgsConstructor
public class CouponCountRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public Long increment() {
		return redisTemplate.opsForValue()
			.increment("coupon_count");
	}
}

@Service
@RequiredArgsConstructor
public class ApplyService {

	private final CouponRepository couponRepository;

	private final CouponCountRepository couponCountRepository;

	private final CouponCreateProducer couponCreateProducer;

	public void apply(Long userId) {
		Long count = couponCountRepository.increment();

		if (count > 100) {
			return;
		}

		couponCreateProducer.create(userId);
	}

}
```

이후 토픽 생성

```
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic coupon_create
```



컨슈머 실행

```
docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
```



## 컨슈머 애플리케이션 작성

```java
@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, Long> consumerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");

		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Long> kafkaListenerContainerFactory() {
		final ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(consumerFactory());

		return factory;
	}

}


@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;

	public CouponCreatedConsumer(final CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	@KafkaListener(topics = "coupon_create", groupId = "group_1")
	public void listener(Long userId) {

		System.out.println(userId);

		couponRepository.save(new Coupon(userId));
	}

}

```



테스트시 실시간으로 생성되는게 아니여서 넉넉히 sleep 10초로 잡자.

```java
@Test
void 여러명응모() throws InterruptedException {
	int threadCount = 1000;
	final ExecutorService executorService = Executors.newFixedThreadPool(32);
	final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
	for (int i = 0; i < threadCount; i++) {
		long userId = i;
		executorService.submit(() -> {
			try {
				applyService.apply(userId);
			} finally {
				countDownLatch.countDown();
			}
		});
	}
	countDownLatch.await();
	Thread.sleep(1000);
	final long count = couponRepository.count();
	assertThat(count).isEqualTo(100);
}
```



# 컨슈머에서 에러가 발생하면?


# SpringBoot2버전 Redis 테스트 컨테이너



```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    testImplementation "org.testcontainers:vault:${testcontainersVersion}"
    testImplementation 'org.testcontainers:junit-jupiter'
}

dependencyManagement {
    imports {
//        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
        mavenBom "org.testcontainers:testcontainers-bom:${testcontainersVersion}"
    }
}


```



```java
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class FastcampusSpringBootPracticeApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(FastcampusSpringBootPracticeApplicationTests.class);

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:latest"));

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    static void setup() {
        redisContainer.followOutput(new Slf4jLogConsumer(logger));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Test
    void contextLoads() throws Exception {
        // Given

        // When
        GenericContainer.ExecResult execResult1 = redisContainer.execInContainer("redis-cli", "get", "student:cassie");
        GenericContainer.ExecResult execResult2 = redisContainer.execInContainer("redis-cli", "get", "student:fred");
        GenericContainer.ExecResult execResult3 = redisContainer.execInContainer("redis-cli", "get", "student:jack");
        Student actual1 = mapper.readValue(execResult1.getStdout(), Student.class);
        Student actual2 = mapper.readValue(execResult2.getStdout(), Student.class);
        Student actual3 = mapper.readValue(execResult3.getStdout(), Student.class);

        // Then
        assertThat(redisContainer.isRunning()).isTrue();
        assertThat(actual1).isEqualTo(Student.of("cassie", 18, Student.Grade.A));
        assertThat(actual2).isEqualTo(Student.of("fred", 14, Student.Grade.C));
        assertThat(actual3).isEqualTo(Student.of("jack", 15, Student.Grade.B));
    }

}

```



```java
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith(name -> name + ":")
                .entryTtl(Duration.ofSeconds(10))
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

}

```


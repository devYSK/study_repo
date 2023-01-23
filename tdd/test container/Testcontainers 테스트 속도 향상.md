https://www.merikan.com/2020/10/speed-up-your-testcontainers-tests/





[이것은 Testcontainers](https://www.testcontainers.org/) 에 대한 두 부분으로 구성된 시리즈의 두 번째 부분입니다 . 이 부분에서는 무엇보다도 Testcontainer를 훨씬 빠르게 시작할 수 있는 몇 가지 요령을 보여 드리겠습니다.

# 소개

이전 블로그 게시물인 [Getting Started with Testcontainers 에서 ](https://merikan.com/2020/10/getting-started-with-testcontainers/)[Testcontainers](https://www.testcontainers.org/) 프레임워크가 무엇 이고 프로젝트에서 이를 사용하는 방법을 설명했습니다. Testcontainers를 오랫동안 사용하면서 더 좋고 더 빠르게 만들기 위해 조정해야 할 사항이 있다는 것을 알게 되었습니다. 아래에서는 환상적인 프레임워크를 훨씬 더 좋게 만드는 몇 가지 사항을 보여드리겠습니다.

## 소스 코드

내 예제 프로젝트의 전체 소스 코드는 [https://github.com/merikan/testcontainers-demo)](https://github.com/merikan/testcontainers-demo) 및 폴더 에서 찾을 수 있습니다 `faster`.

# 싱글톤 패턴 사용

[이전 블로그 게시물](https://merikan.com/2020/10/getting-started-with-testcontainers/) 에서 이에 대해 설명 했지만 반복할 가치가 있습니다. `@Container`때로는 통합 테스트 클래스에서 Testcontainer를 선언하기 위해 주석을 사용하는 이유가 있을 수 있습니다 . 이것의 단점은 테스트 컨테이너가 실행되는 각 테스트 클래스 사이에서 시작 및 중지되어 매우 오랜 시간이 걸릴 수 있다는 것입니다. 대신 싱글톤 패턴을 사용하여 Testcontainer가 모든 테스트 클래스에 대해 한 번만 시작하고 완료되면 중단되도록 할 수 있습니다.

동일한 리소스를 사용하는 여러 테스트로 인해 발생하는 모든 부작용을 처리해야 한다는 점을 명심하십시오. 예를 들어 리소스 상태에 영향을 받지 않는 테스트를 만들거나 각 테스트에 대해 원하는 상태로 리소스를 설정합니다.

싱글톤 패턴을 사용하기 위해 Testcontainer 생성을 처리하는 추상 클래스를 만들 수 있습니다.

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integrationtest")
public abstract class AbstractIntegrationTest {
    private static final DockerImageName MARIADB_IMAGE = DockerImageName.parse("mariadb:10.5.5");
    private static final MariaDBContainer mariadb;

    static {
        mariadb = new MariaDBContainer<>(MARIADB_IMAGE)
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
        mariadb.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }
}
```

그런 다음 각 통합 테스트 클래스는 이 추상 클래스에서 상속하여 Testcontainers를 사용해야 합니다.

```java
public class TodoServiceImplIT extends AbstractIntegrationTest {

    @Autowired
    private TodoService uut;

    @Autowired
    private TodoRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository.deleteAll();
    }
```

이제 테스트를 실행하면 Testcontainer가 한 번만 시작되는 것을 볼 수 있습니다. 즉, 테스트 스위트를 실행하는 시간이 크게 단축됩니다.

# 여러 컨테이너로 시작 시간 단축

현재 프로젝트에는 테스트 스위트에 여러 컨테이너가 있으며 한 번만 완료하더라도 시작하는 데 시간이 걸립니다. 그러나 약간의 변경으로 시작 시간을 줄일 수 있습니다.

먼저 여러 Testcontainer를 시작하는 예제를 만든 다음 시작 시간을 줄일 수 있는지 확인하기 위해 일부 변경을 수행하겠습니다. 잘못된 결과를 얻지 않으려면 모든 이미지를 풀다운하고 로컬에서 사용할 수 있도록 먼저 한 번 실행해야 합니다. 예제를 깨끗하고 명확하게 유지하기 위해 시작된 컨테이너의 속성을 사용하지 않습니다.

```java
static {
    Instant start = Instant.now();

    mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
    mariadb1.start();
    mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
    mariadb2.start();
    redis = new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);
    redis.start();
    kafka = new KafkaContainer(KAFKA_IMAGE);
    kafka.start();
    sftp = new GenericContainer<>(SFTP_IMAGE)
        .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"));
    sftp.start();

    log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));
    
}
```

이제 하나의 테스트를 실행하면 5개의 Testcontainer를 모두 시작하는 데 30초도 채 걸리지 않는다는 것을 알 수 있습니다.

```
 🐳 TestContainers started in PT29.836S 
```

이제 몇 가지 사항을 변경할 시간입니다. 위의 코드에서 볼 수 있듯이 테스트 컨테이너를 순차적으로 시작하고 있습니다. 대신 병렬로 시작할 수 있다면 어떨까요? 시도해 봅시다. 이를 위해 Stream 클래스를 사용할 수 있으며 다음과 같이 표시됩니다.

```java
static {
    Instant start = Instant.now();

    mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
    mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
    redis = new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);
    kafka = new KafkaContainer(KAFKA_IMAGE);
    sftp = new GenericContainer<>(SFTP_IMAGE)
        .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"));
    Stream.of(mariadb1, mariadb2, redis, kafka, sftp).parallel().forEach(GenericContainer::start);

    log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));
}
```

이제 테스트를 다시 실행하면 5개의 Testcontainer를 모두 시작하는 데 12초도 채 걸리지 않는다는 것을 알 수 있습니다.

```
 🐳 TestContainers started in PT11.606S
```

그래서 시작 시간을 18초 단축했습니다. 그렇게 나쁘지는 않죠? 테스트를 더 빠르게 하기 위해 할 수 있는 다른 방법이 있습니까? 예, 있습니다. 계속 읽으면 또 다른 방법을 볼 수 있습니다.

# 테스트 컨테이너 재사용

반복적으로 속도를 저하시키는 한 가지는 하나의 테스트로 작업하고 여러 번 실행하고 싶을 때입니다. 시간이 오래 걸릴 수 있으며 빠른 피드백 루프를 원합니다. 그럼 좀 더 깊이 파고들어 어떻게 해결할 수 있는지 알아봅시다.

Testcontainers에는 이미 시작된 Testcontainer를 재사용할 수 있는 [미리 보기 기능 이 있습니다. ](https://github.com/testcontainers/testcontainers-java/pull/1781)즉, Ryuk(테스트 실행 후 Testcontainer를 종료하는 정리 메커니즘)가 시작되지 않으며, 이는 테스트가 완료된 후에도 Testcontainer가 중단되지 않는다는 것을 의미합니다. 이 기능은 개발 과정에서 시간을 정말 절약할 수 있습니다! 기억해야 할 것은 작업이 완료되면 모든 Testcontainer를 수동으로 중지하는 것입니다.

이 재사용을 가능하게 하려면 두 가지 작업을 수행해야 합니다. 먼저 메서드를 사용하여 Testcontainer에 재사용해야 한다고 알려야 합니다 `.withReuse(true)`. `~/.testcontainers.properties`다음으로 홈 디렉터리에 Testcontainers()에 대한 구성 파일을 만들고 속성을 추가해야 합니다 `testcontainers.reuse.enable=true`. 작성 시점에 이 속성 파일은 프로젝트당 클래스 경로가 아닌 홈 디렉토리에만 있을 수 있습니다. 그러나 다른 한편으로는 팀 구성원이나 CI/CD 파이프라인과 같은 다른 환경에 이러한 동작을 강요하고 싶지 않을 것입니다. 이것은 반드시 선택 사항이어야 하며 프로젝트별로 추가할 수 있게 되면 `.gitignore`동료 및 CI/CD 파이프라인에 전파되지 않도록 추가해야 합니다.

활성화할 때 추상 클래스가 다음과 같이 표시됩니다 `reuse`.

```java
static {
    Instant start = Instant.now();

    mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
        .withReuse(true);
    mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
        .withReuse(true);
    redis = new GenericContainer<>(REDIS_IMAGE)
        .withExposedPorts(6379)
        .withReuse(true);
    kafka = new KafkaContainer(KAFKA_IMAGE)
        .withReuse(true);
    sftp = new GenericContainer<>(SFTP_IMAGE)
        .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"))
        .withReuse(true);
    Stream.of(mariadb1, mariadb2, redis, kafka, sftp).parallel().forEach(GenericContainer::start);

    log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));
}
```

아마도 직면하게 될 한 가지 [문제](https://github.com/testcontainers/testcontainers-java/issues/2961) 는 Testcontainer가 이 특정 프로젝트에 고유하지 않다는 것입니다. 여러 프로젝트에 동일한 구성을 가진 여러 Testcontainer가 있기 때문에 이것은 나에게 실질적인 문제를 제기했습니다. 이것을 수정합시다.

[소스 코드](https://github.com/testcontainers/testcontainers-java/blob/17b4f6c136f6f2c7dc223bad407221f62a8f0088/core/src/main/java/org/testcontainers/containers/GenericContainer.java#L364-L412) 를 살펴보면 시작된 Testcontainer의 고유성은 실제 구성을 기반으로 계산됩니다. Testcontainers는 레이블이 있는 현재 실행 중인 모든 컨테이너 중에서 해시를 검색 `org.testcontainers.hash`하여 일치하는 항목이 있는지 확인하고 일치하는 경우 이 컨테이너를 재사용합니다. 이 문제에 대한 쉬운 해결책은 이 특정 컨테이너를 고유하게 만드는 구성에 무언가를 추가하는 것입니다. [값으로 고유한 UUID](https://www.uuidgenerator.net/)`reuse.UUID` 가 있는 이름 을 사용하여 나만의 레이블을 추가하도록 선택했습니다 .

```java
.withLabel("reuse.UUID", "e06d7a87-7d7d-472e-a047-e6c81f61d2a4");
```

이것은 Testcontainer에 대한 고유 ID로 재사용을 활성화할 때 추상 클래스에서 보이는 모습입니다.

```java
static {
    Instant start = Instant.now();

    mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
        .withReuse(true)
        .withLabel("reuse.UUID", "e06d7a87-7d7d-472e-a047-e6c81f61d2a4");
    mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
        .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
        .withReuse(true)
        .withLabel("reuse.UUID", "282b8993-097c-4fd4-98f1-94daf3466dd6");
    redis = new GenericContainer<>(REDIS_IMAGE)
        .withExposedPorts(6379)
        .withReuse(true)
        .withLabel("reuse.UUID", "0429783b-c855-4b32-8239-258cba232b63");
    kafka = new KafkaContainer(KAFKA_IMAGE)
        .withReuse(true)
        .withLabel("reuse.UUID", "f8724ec0-2f66-4684-80cd-1b24a7399366");
    sftp = new GenericContainer<>(SFTP_IMAGE)
        .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"))
        .withReuse(true)
        .withLabel("reuse.UUID", "0293a405-e435-4f03-9e4b-b6160d9e60fe");
    Stream.of(mariadb1, mariadb2, redis, kafka, sftp).parallel().forEach(GenericContainer::start);

    log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));
}
```

테스트를 다시 시작하면 컨테이너를 처음 시작하는 데 약 11초가 걸렸습니다. 이제 Testcontainer가 재사용되며 이번에는 약 1초만 걸립니다. 나쁘지 않죠?

```
 🐳 TestContainers started in PT1.796S
```

이것은 테스트를 실행할 때 더 빠른 피드백을 얻을 수 있는 성능 향상입니다. 이제 테스트가 끝나면 실행 중인 Testcontainer를 중지하기만 하면 됩니다.

실행 중인 모든 Testcontainer를 나열하려면:

```sh
$ docker ps --filter label=org.testcontainers
CONTAINER ID        IMAGE                         COMMAND                  CREATED             STATUS              PORTS                                                                       NAMES
e1ccf87be0f2        mariadb:10.5.5                "docker-entrypoint.s…"   3 minutes ago       Up 3 minutes        0.0.0.0:32969->3306/tcp                                                     adoring_pascal
bed6d13ccf33        atmoz/sftp                    "/entrypoint SFTP_US…"   3 minutes ago       Up 3 minutes        0.0.0.0:32967->22/tcp                                                       serene_driscoll
4732e1e5144b        confluentinc/cp-kafka:5.2.1   "sh -c 'while [ ! -f…"   3 minutes ago       Up 3 minutes        0.0.0.0:32968->2181/tcp, 0.0.0.0:32966->9092/tcp, 0.0.0.0:32965->9093/tcp   tender_wright
dae6f7e12707        redis:5.0.5                   "docker-entrypoint.s…"   3 minutes ago       Up 3 minutes        0.0.0.0:32964->6379/tcp                                                     epic_goldberg
58442bb5df71        mariadb:10.5.5                "docker-entrypoint.s…"   3 minutes ago       Up 3 minutes        0.0.0.0:32970->3306/tcp                                                     heuristic_varahamihira
```

실행 중인 모든 Testcontainer를 중지하려면:

```sh
$  docker stop $(docker ps -q --filter label=org.testcontainers)
e1ccf87be0f2
bed6d13ccf33
4732e1e5144b
dae6f7e12707
58442bb5df71
```

# 도커 컨테이너에서 테스트 실행

자주 제기되는 한 가지 질문은 다음과 같습니다. 테스트가 이제 도커 컨테이너를 사용하는 경우 테스트 자체가 도커 컨테이너에서 실행될 수 있습니까? 예, 가능합니다 . [Docker에서는 Docker](https://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/) 라고도 합니다. DinD. docker in docker를 사용하면 권한 모드 등과 같은 새로운 문제가 있을 수 있지만 여기에서 다루려는 것은 아닙니다.

Docker 컨테이너에서 로컬로 테스트를 실행합니다.

```sh
$ git clone https://github.com/merikan/testcontainers-demo.git
$ cd testcontainers-demo/faster
faster $  docker run -it --rm -v $PWD:$PWD -w $PWD -v /var/run/docker.sock:/var/run/docker.sock maven:3 ./mvnw clean test verify
```

[여기](https://www.testcontainers.org/supported_docker_environment/continuous_integration/dind_patterns) 에서 Docker의 Testcontainers 및 Docker에 대한 자세한 내용을 읽을 수 있습니다 .

# 요약

이 블로그 게시물에서는 Testcontainers를 사용하여 테스트를 실행하는 시간을 크게 줄이는 몇 가지 방법을 보여 주었습니다.

그리고 마지막으로;
나는 단지 인간입니다. 모호함, 오타, 오류 또는 가능한 개선 사항이 있으면 알려주십시오.
이 블로그 게시물이 마음에 들면 댓글을 추가하고 좋아하는 소셜 미디어 플랫폼을 사용하여 이 블로그 게시물을 자유롭게 공유하세요! 시작하려면 아래에 몇 가지 아이콘이 있어야 합니다.

다음 시간까지 코딩을 계속하고 새로운 것을 배우는 것을 멈추지 마세요!

## 자원

- [Java 프로젝트](https://github.com/testcontainers/testcontainers-java/) 용 Testcontainers [홈페이지](https://www.testcontainers.org/) 및 소스 코드
- 내 예제 프로젝트의 전체 소스 코드는 [https://github.com/merikan/testcontainers-demo)](https://github.com/merikan/testcontainers-demo) 및 폴더 에서 찾을 수 있습니다 `faster`.
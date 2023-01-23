https://medium.com/@jojiapp/junit-localdatetime-now-mocking-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0-fb24119976f1



# [Junit] LocalDateTime.now Mocking 처리하기

개발을 하다보면 현재 시간을 넣는 로직이 필연적으로 존재합니다. 가령 마지막으로 패스워드 변경한 날짜 기능만 해도 LocalDateTime.now() 메서드를 필요로 합니다.

하지만 LocalDateTime.now()를 사용하게 되면 밀리초 단위로 값이 나오기 때문에 테스트가 실패하게 됩니다.

그래서 나름대로 여러가지 방안을 생각해 보았습니다.

> 소스는 [GitHub](https://github.com/jojiapp/local-date-time-mock-test)에 올려두었습니다.

# 방법 1

현재 시간을 파라미터로 받는 것입니다.

```
public class Password {
    
    private final String value;
    private final LocalDateTime passwordLastModifiedAt;

    public Password(String value, LocalDateTime passwordLastModifiedAt) {
        this.value = value;
        this.passwordLastModifiedAt = passwordLastModifiedAt;
    }

}
```

위 처럼 외부에서 주입을 받으면 외부에서 생성한 값과 주입된 값이 같은지 비교가 가능하니 테스트가 가능합니다.

## 단점

계속해서 LocalDateTime.now()를 외부로 밀어내게 되고 결국 어딘가는 처리를 해야 하는 상황이 되어 바람직하지 않은 것 같았습니다.

# 방법 2

별도의 클래스로 만들어 Mock으로 처리하여 사용하는 것입니다.

```
@Component
public class LocalDateTimeNow {

    public LocalDateTime now() {

        return LocalDateTime.now();
    }
}
```

이제 테스트 코드에서 LocalDateTimeNow를 Mock으로 만들어 직접 만든 값으로 대체 하여 사용할 수 있습니다.

## 단점

모든 곳에서 LocalDateTime.now()를 사용할거라면 위 컴포넌트를 주입 받아 사용해야 한다는 것 자체가 마음에 들지 않았습니다.

# 방법 3

LocalDateTime.now()를 static mock 처리를 하여 사용하는 것입니다. 이 방법은 나이스하다 생각했지만, Mockito의 근본인 mockito-core는 static mock을 지원하지 않았습니다.

별도의 라이브러리를 받으면 사용이 가능하나, private 메서드도 테스트가 가능한 지경이 되기에 올바르지 않다고 생각했습니다.

또한 static mock을 사용해야 한다면 설계가 잘못된 것은 아닌지 의심해 봐야한다는 말도 있기에 이 방법은 아니다 싶었습니다.

# 방법 4 (결론적으로 이 방법을 사용)

LocalDateTime.now()는 Clock을 받는 메서드도 존재합니다.

```
public static LocalDateTime now(Clock clock) {
    Objects.requireNonNull(clock, "clock");
    final Instant now = clock.instant();  // called once
    ZoneOffset offset = clock.getZone().getRules().getOffset(now);
    return ofEpochSecond(now.getEpochSecond(), now.getNano(), offset);
}
```

그렇다면 Clock을 별도의 빈으로 만들어 Clock을 Mocking하여 우리가 원하는 시간을 넣어 테스트할 수 있습니다.

```
@Configuration
public class ClockConfig {
    
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
```

이제 사용하는 쪽에서는 Clock을 의존 주입받아 넣어주면 됩니다.

```
public class Password {

    private final String value;
    private final LocalDateTime passwordLastModifiedAt;

    public Password(String value, LocalDateTime passwordLastModifiedAt) {
        this.value = value;
        this.passwordLastModifiedAt = passwordLastModifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value) && Objects.equals(passwordLastModifiedAt, password.passwordLastModifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, passwordLastModifiedAt);
    }
}
@Service
public class PasswordService {

    private final Clock clock;

    public PasswordService(Clock clock) {
        this.clock = clock;
    }
    
    public Password updatePassword(final String password) {

        return new Password(password, LocalDateTime.now(clock));
    }
}
```

## 테스트 코드 작성

```
@SpringBootTest
class PasswordServiceTest {

    @Autowired
    private PasswordService passwordService;

    @MockBean
    private Clock clock;

    @Test
    @DisplayName("패스워드가 변경 시간이 현재 시간으로 변경된다.")
    void test1() throws Exception {
        // Given
        final var password = "password";

        final var now = Instant.now();
        given(clock.instant()).willReturn(now);
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        // When
        final var updatePassword = passwordService.updatePassword(password);

        // Then
        final var newPassword = new Password(password, LocalDateTime.now(clock));
        assertThat(updatePassword).isEqualTo(newPassword);
    }

}
```

위 처럼 Clock에서 필요한 메서드를 Mock 처리하여 진행하면 됩니다.

# 왜 이 방식인가?

Clock이나 직접 LocalDateTime.now()를 가진 빈을 생성하나 크게 차이 없어보이는데 왜 Clock을 사용하는지 궁금 할수도 있습니다.

직접 만들어 쓴다면 이런 규칙을 모르는 사람은 혼돈이 올 수 있습니다. 보통은 “**현재 시간 = LocalDateTime.now()**” 이니까요.

## Clock을 사용하는걸 모르면 별 수 없지 않나?!

Oracle 문서를 보면 now(Clock)를 테스트에서 대안점으로 사용된다고 설명이 되어 있습니다.

즉, 직접 만든 것 보다야 조금 더 공신력이 있기에 해당 방식을 사용하기로 채택하였습니다.

# 참고 사이트

- https://yeonyeon.tistory.com/258
# Spring security - Role vs Authority (hasRole? hasAuthority?)



### Role과 Authority의 사전적 의미

* Role : 역할

* Authority : 권한



이 둘은 비슷하면서도 다르다.  

* 역할은 커뮤니티 관리자가 권한을 그룹화하고 사용자 또는 사용자 그룹에 권한을 지정하는 방법을 제공한다. 
* 권한은 커뮤니티에서 사용자가 수행할 수 있는 조치를 정의한다.

  


소규모 프로젝트에서는 Role이나 Authority 둘 중 한가지로 사용해서도 충분히 접근제어를 할 수 있지만, 규모가 커지고 요구사항이 복잡해진다면 역할과 권한을 분리하여 명시함으로써 유지보수성도 높아질 것이다.  


Spring Security는 메소드 수준에서 권한 부여 의미를 지원하는데, 이 둘이 차이가 있다.    




또한, **Web Security Expressions** 에서는 다음과 같은 메서드들을 지원한다.

- *hasRole*, *hasAnyRole*
- *hasAuthority*, *hasAnyAuthority*
- *permitAll*, *denyAll*
- *isAnonymous*, *isRememberMe*, *isAuthenticated*, *isFullyAuthenticated*
- *principal*, *authentication*
- *hasPermission*





> ROLE의 경우에는 접두어 ROLE_을 `변수`에 넣어줘야한다는 불편함이 있다. 이것 때문에 Authority를 사용하는 경우가 많다. 



## Role With PreAuthorize



Spring Security에서 Role로 인증을 할 때에는 prefix로 `ROLE_` 이라는 문자를 붙여줘야 한다.  

* DB에 저장하거나, enum의 name이나 value에 ROLE_XXX가 붙어야 한다.



먼저 다음과 같이 Role을 정의한다



```java
public enum Role {
  
  ADMIN("ROLE_ADMIN"),
  MANAGER("ROLE_MANAGER");
  
  private final String name;
  
}
```



다음 SecurityConfig에서 다음과 같이 구성한다.



```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) {
        http.httpBasic().disable()
						
            ...
          
            .authorizeRequests()
						.antMatchers("/admin/*").hasRole(Role.ADMIN.name())
			      .antMatchers("/auth/").hasAnyRole(Role.values())

            .anyRequest().authenticated()
						
          ...
            ;

        return http.build();
    }


}
```

* 이 Config 예는 /admin/* 으로 시작하는 모든 요청에 에 대해 `ROLE_ADMIN` 을 갖고 있는지 검사한다는 의미이다. 

* /auth/** 으로 시작하는 모든 요청에 대해서는 Role 중 아무거나 검사한다는 의미이다.

  


메소드에서는 다음과 같이  둘 중 하나의 방법으로 메서드 실행을 제한할 수 있다.

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> get(){
  ...
}

// 또는

@PreAuthorize("hasRole('ROLE_ADMIN')")
public ResponseEntity<?> get(){
  ...
}
```

* hasRole('권한') 만 적어줘도 접두어로 자동으로 ROLE_이 붙기 때문에 검사할 수 있다. 



따져보면 Role이나 Authority나 모두 "권한" 정보이다. 그리고 hasRole 안에 "ROLE_" prefix를 직접 넣어주면 spring security가 넣어줘서 판단하니 직접 넣지 말라는 컴파일 에러도 발생한다.



## Authority With PreAuthorize



Authority로 인증을 할 때에는 접두어가 없어도 된다.

  


먼저 다음과 같이 Authority를 정의한다.

```java
public enum Authority {
  
  ADMIN,
  MANAGER;
  
}
```

  


다음 SecurityConfig에서 다음과 같이 구성한다.

```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) {
        http.httpBasic().disable()
						
            ...
          
            .authorizeRequests()
						.antMatchers("/admin/**").hasAuthority("ADMIN")
			      .antMatchers("/auth/**").hasAnyAuthority(Authority.values())

            .anyRequest().authenticated()
						
          ...
            ;

        return http.build();
    }


}
```

* 이 Config 예는 /admin/* 으로 시작하는 모든 요청에 에 대해 Admin Authority을 갖고 있는지 검사한다는 의미이다. 

* /auth/** 으로 시작하는 모든 요청에 대해서는 Authority중 아무거나 검사한다는 의미이다.




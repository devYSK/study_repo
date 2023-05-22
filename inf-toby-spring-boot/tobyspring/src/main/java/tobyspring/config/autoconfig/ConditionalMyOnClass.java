package tobyspring.config.autoconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

// 이런 클래스가 있으면 이 조건을 사용해라
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Conditional(MyOnClassCondition.class)
public @interface ConditionalMyOnClass {

	String value(); // 클래스의 이름을 전달
}

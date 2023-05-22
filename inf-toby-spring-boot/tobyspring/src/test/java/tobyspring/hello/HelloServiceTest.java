package tobyspring.hello;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@UnitTest
@interface FastUnitTest {

}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Test
@interface UnitTest {
}

class HelloServiceTest {
	@Test
	void simpleHelloService() {
		SimpleHelloService helloService = new SimpleHelloService (helloRepositoryStub);
		String ret = helloService. sayHello ("Test");
		Assertions.assertThat ( ret) . isEqualTo ("Hello Test");
	}

	private static HelloRepository helloRepositoryStub = new HelloRepository() {
		@Override
		public Hello findHello(String name) {
			return null;
		}

		@Override
		public void increaseCount(String name) {

		}
	};

}
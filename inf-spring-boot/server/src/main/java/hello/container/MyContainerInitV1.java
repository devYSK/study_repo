package hello.container;

import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public class MyContainerInitV1 implements ServletContainerInitializer {
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		System.out.println("MyContainerInitV1.onStartup");
		System.out.println("MyContainerInitV1 c = " + c);
		System.out.println("MyContainerInitV1 ctx = " + ctx);
	}
}

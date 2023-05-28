package hello.container;

import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

@HandlesTypes(AppInit.class)
public class MyContainerInitV2 implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws
		ServletException {
		System.out.println("MyContainerInitV2.onStartup");
		System.out.println("MyContainerInitV2 c = " + c);
		System.out.println("MyContainerInitV2 container = " + ctx);
		for (Class<?> appInitClass : c) {
			try {
				//new AppInitV1Servlet()과 같은 코드
				AppInit appInit = (AppInit)
					appInitClass.getDeclaredConstructor().newInstance();
				appInit.onStartup(ctx);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}

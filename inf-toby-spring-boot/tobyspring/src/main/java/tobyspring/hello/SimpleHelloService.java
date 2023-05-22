package tobyspring.hello;

import org.springframework.stereotype.Service;

@Service
public class SimpleHelloService implements HelloService {

	private final HelloRepository helloRepository;

	public SimpleHelloService(HelloRepository helloRepository) {
		this.helloRepository = helloRepository;
	}

	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}

}

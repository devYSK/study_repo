package com.ys.springboot_blog_hodol;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.ys.springboot_blog_hodol.domain.User;
import com.ys.springboot_blog_hodol.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithTestUserContextFactory implements WithSecurityContextFactory<WithTestUser> {

	private final UserRepository userRepository;

	@Override
	public SecurityContext createSecurityContext(WithTestUser annotation) {

		System.out.println("lgtm!!");
		System.out.println("lgtm!!");
		System.out.println("lgtm!!");
		System.out.println("lgtm!!");
		System.out.println("lgtm!!");
		System.out.println("lgtm!!");

		final Optional<User> byId = userRepository.findById(Long.valueOf(annotation.id()));

		if (byId.isEmpty()) {
			System.out.println("lgtm1234567!!");
		}

		return null;
	}

}

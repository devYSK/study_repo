package com.ys.moduleapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ys.common.domain.Member;
import com.ys.common.enums.CodeEnum;
import com.ys.common.repository.MemberRepository;
import com.ys.common.service.CommonDemoService;
import com.ys.moduleapi.exceptionhandler.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemoService {

	@Value("${profile-name}")
	private String name;

	private final MemberRepository memberRepository;

	private final CommonDemoService commonDemoService;

	public String save() {
		System.out.println("env profile : " + name);
		System.out.println("commonDemoService.getName() : " + commonDemoService.getModuleName());

		Member newMember = memberRepository.save(Member.builder()
													   .name(Thread.currentThread()
																   .getName())
													   .build());
		return newMember.getName();
	}

	public String find() {
		return String.valueOf(memberRepository.findAll()
											  .size());
	}

	public String handleCustomException() {
		if (true) {
			System.out.println("Throw CustomException");
			throw new CustomException(CodeEnum.NOT_IDENTITY_VERIFIED_USER);
		}

		return "exception";
	}
}
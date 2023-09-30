package com.ys.jpa.member;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ys.common.domain.Member;
import com.ys.common.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

	private final MemberJpaRepository memberJpaRepository;

	@Override
	public Member findById(Long id) {
		System.out.println("i`m jpaRepository");
		return memberJpaRepository.findById(id).get();
	}

	@Override
	public Member save(Member member) {

		System.out.println("i`m jpaRepository");
		return memberJpaRepository.save(member);
	}

	@Override
	public List<Member> findAll() {
		System.out.println("i`m jpaRepository");
		return memberJpaRepository.findAll();
	}

}

package com.ys.common.repository;

import java.util.List;

import com.ys.common.domain.Member;

public interface MemberRepository {

	Member findById(Long id);

	Member save(Member member);

	List<Member> findAll();

}

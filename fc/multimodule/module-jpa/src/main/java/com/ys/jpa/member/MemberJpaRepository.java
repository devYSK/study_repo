package com.ys.jpa.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ys.common.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}

package com.ys.test.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : ysk
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}

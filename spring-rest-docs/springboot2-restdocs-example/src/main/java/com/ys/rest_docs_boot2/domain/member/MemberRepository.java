package com.ys.rest_docs_boot2.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select (count(m) > 0) from Member m where m.nickName = ?1")
    boolean existsByNickName(
        String nickName);

    Optional<Member> findByNickName(String nickName);

}

package com.ys.membershipservice.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.ys.membershipservice.domain.Membership;

@Component
public class MembershipMapper {
    public Membership mapToDomainEntity(MembershipJpaEntity membershipJpaEntity) {
        return Membership.generateMember(
                new Membership.MembershipId(membershipJpaEntity.getMembershipId()+""),
                new Membership.MembershipName(membershipJpaEntity.getName()),
                new Membership.MembershipEmail(membershipJpaEntity.getEmail()),
                new Membership.MembershipAddress(membershipJpaEntity.getAddress()),
                new Membership.MembershipIsValid(membershipJpaEntity.isValid()),
                new Membership.MembershipIsCorp(membershipJpaEntity.isCorp()),
            new Membership.MembershipRefreshToken(membershipJpaEntity.getRefreshToken())
        );
    }
}

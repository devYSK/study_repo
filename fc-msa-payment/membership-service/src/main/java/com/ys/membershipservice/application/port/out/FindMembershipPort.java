package com.ys.membershipservice.application.port.out;

import com.ys.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.ys.membershipservice.domain.Membership;

public interface FindMembershipPort {
    MembershipJpaEntity findMembership(
            Membership.MembershipId membershipId
    );
}

package com.ys.membershipservice.application.port.out;

import com.ys.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.ys.membershipservice.domain.Membership;

public interface RegisterMembershipPort {

    MembershipJpaEntity createMembership(
        Membership.MembershipName membershipName
        , Membership.MembershipEmail membershipEmail
        , Membership.MembershipAddress membershipAddress
        , Membership.MembershipIsValid membershipIsValid
            ,Membership.MembershipIsCorp membershipIsCorp
    );
}

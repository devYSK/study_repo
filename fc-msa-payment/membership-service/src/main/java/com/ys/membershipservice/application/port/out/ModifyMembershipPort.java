package com.ys.membershipservice.application.port.out;

import com.ys.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.ys.membershipservice.domain.Membership;

public interface ModifyMembershipPort {

    MembershipJpaEntity modifyMembership(
            Membership.MembershipId membershipId
        ,Membership.MembershipName membershipName
        , Membership.MembershipEmail membershipEmail
        , Membership.MembershipAddress membershipAddress
        , Membership.MembershipIsValid membershipIsValid
            ,Membership.MembershipIsCorp membershipIsCorp
            ,Membership.MembershipRefreshToken membershipRefreshToken
    );
}

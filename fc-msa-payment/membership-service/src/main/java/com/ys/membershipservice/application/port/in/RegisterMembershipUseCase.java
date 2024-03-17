package com.ys.membershipservice.application.port.in;


import com.fastcampuspay.membership.domain.Membership;

public interface RegisterMembershipUseCase {
    Membership registerMembership(RegisterMembershipCommand command);
}

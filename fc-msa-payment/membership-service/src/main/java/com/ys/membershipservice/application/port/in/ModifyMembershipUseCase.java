package com.ys.membershipservice.application.port.in;

import com.ys.membershipservice.domain.Membership;

public interface ModifyMembershipUseCase {
    Membership modifyMembership(ModifyMembershipCommand command);
}

package com.ys.membershipservice.application.port.in;

import com.ys.membershipservice.domain.Membership;

public interface FindMembershipUseCase {
	Membership findMembership(FindMembershipCommand command);
}

package com.ys.membershipservice.application.port.in;

import com.ys.membershipservice.common.SelfValidating;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public
class FindMembershipCommand extends SelfValidating<FindMembershipCommand> {
    private final String membershipId;

}

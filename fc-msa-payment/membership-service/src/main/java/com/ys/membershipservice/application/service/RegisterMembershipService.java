package com.ys.membershipservice.application.service;

import org.springframework.transaction.annotation.Transactional;

import com.ys.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.ys.membershipservice.adapter.out.persistence.MembershipMapper;
import com.ys.membershipservice.application.port.in.RegisterMembershipCommand;
import com.ys.membershipservice.application.port.in.RegisterMembershipUseCase;
import com.ys.membershipservice.application.port.out.RegisterMembershipPort;
import com.ys.membershipservice.common.UseCase;
import com.ys.membershipservice.domain.Membership;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisterMembershipService implements RegisterMembershipUseCase {

    private final RegisterMembershipPort registerMembershipPort;
    private final MembershipMapper membershipMapper;

    @Override
    public Membership registerMembership(RegisterMembershipCommand command) {
        MembershipJpaEntity jpaEntity = registerMembershipPort.createMembership(
                new Membership.MembershipName(command.getName()),
                new Membership.MembershipEmail(command.getEmail()),
                new Membership.MembershipAddress(command.getAddress()),
                new Membership.MembershipIsValid(command.isValid()),
                new Membership.MembershipIsCorp(command.isCorp())
        );
        // entity -> Membership
        return membershipMapper.mapToDomainEntity(jpaEntity);
    }

}
